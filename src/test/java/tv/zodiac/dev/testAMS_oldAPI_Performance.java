package tv.zodiac.dev;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * We are localhost (Charter Headend). Full chain of requests: localhost -> AMS -> STB -> AMS -> localhost
 */
class testAMS_oldAPI_Performance extends API_common {
    private OldAPI_AMS AMS = new OldAPI_AMS();
    private int timeout = 20000;
    private int sleep_after_iteration = 1000;

    private ArrayList add_list,
            delete_list = new ArrayList(),
            purge_list = new ArrayList();
    private int a_avg = 0, a_med = 0, a_min = 0, a_min_iteration = 0, a_max = 0, a_max_iteration = 0, a_size = 0,
            d_avg = 0, d_med = 0, d_min = 0, d_min_iteration = 0, d_max = 0, d_max_iteration = 0, d_size = 0,
            p_avg = 0, p_med = 0, p_min = 0, p_min_iteration = 0, p_max = 0, p_max_iteration = 0, p_size = 0;

    private ArrayList a_current = new ArrayList(),
            d_current = new ArrayList(),
            p_current = new ArrayList();

    private void print_start_header(String ams_ip, String mac, String boxname, int count_reminders, int reminderChannelNumber, int reminderOffset, int reminderOffset_new, int count_iterations) throws IOException {
        check_csv(ams_ip, mac, boxname, count_reminders, reminderChannelNumber, reminderOffset, reminderOffset_new, count_iterations);
        logger(INFO_LEVEL, "[INF] " + new Date() + ": New start for mac=" + mac + "(" + boxname + ") to ams=" + ams_ip + ", "
                + "count_reminders=" + count_reminders + ", "
                + "reminderChannelNumber=" + reminderChannelNumber + ", "
                + "reminderProgramStart=" + reminderProgramStart + ", "
                + "reminderProgramId=" + reminderProgramId + ", "
                + "reminderOffset=" + reminderOffset);
    }

    private void check_csv(String ams_ip, String mac, String boxname, int count_reminders, int reminderChannelNumber, int reminderOffset, int reminderOffset_new, int count_iterations) {
        assertNotNull(ams_ip);
        assertNotNull(mac);
        assertNotNull(boxname);
        assertNotEquals(0, count_reminders);
        assertNotEquals(0, reminderChannelNumber);
        assertNotEquals(null, reminderOffset);
        assertNotEquals(null, reminderOffset_new);
        assertNotEquals(0, count_iterations);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/reminders_oldapi.csv", numLinesToSkip = 1)
    void test10_Add(String ams_ip, String mac, String boxname, int count_reminders, int reminderChannelNumber, int reminderOffset, int reminderOffset_new, int count_iterations) throws IOException, InterruptedException {
        print_start_header(ams_ip, mac, boxname, count_reminders, reminderChannelNumber, reminderOffset, reminderOffset_new, count_iterations);

        for (int i = 1; i <= count_iterations; i++) {
            print_iteration_header(ams_ip, mac, count_reminders, i, count_iterations);

            //reminderChannelNumber = reminderChannelNumber();
            //int finalReminderChannelNumber = reminderChannelNumber;
            add_list = AMS.request_perf(ams_ip, mac, Operation.add, i, count_reminders, reminderChannelNumber, reminderProgramStart, reminderProgramId, reminderOffset);
            print_preliminary_results(add_list);
            if (add_list.get(0).equals(expected200) && add_list.get(1).equals("")) {
                a_current.add(add_list.get(2));
                a_avg = (int) add_list.get(3);
                a_med = (int) add_list.get(4);
                a_min = (int) add_list.get(5);
                a_min_iteration = (int) add_list.get(6);
                a_max = (int) add_list.get(7);
                a_max_iteration = (int) add_list.get(8);
                a_size = (int) add_list.get(9);
            }
            add_list.clear();
            Thread.sleep(sleep_after_iteration);
        }

        prepare_total_results(mac, boxname, count_reminders, count_iterations,
                a_avg, a_med, a_min, a_min_iteration, a_max, a_max_iteration, a_size, a_current,
                0, 0, 0, 0, 0, 0, 0, null,
                d_avg, d_med, d_min, d_min_iteration, d_max, d_max_iteration, d_size, d_current,
                p_avg, p_med, p_min, p_min_iteration, p_max, p_max_iteration, p_size, p_current);
        assertNotEquals(0, a_avg, "a_avg");
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/reminders_oldapi.csv", numLinesToSkip = 1)
    void test11_Add_Purge(String ams_ip, String mac, String boxname, int count_reminders, int reminderChannelNumber, int reminderOffset, int reminderOffset_new, int count_iterations) throws IOException, InterruptedException {
        print_start_header(ams_ip, mac, boxname, count_reminders, reminderChannelNumber, reminderOffset, reminderOffset_new, count_iterations);

        for (int i = 1; i <= count_iterations; i++) {
            print_iteration_header(ams_ip, mac, count_reminders, i, count_iterations);

            add_list = AMS.request_perf(ams_ip, mac, Operation.add, i, count_reminders, reminderChannelNumber, reminderProgramStart, reminderProgramId, reminderOffset);
            print_preliminary_results(add_list);
            if (add_list.get(0).equals(expected200) && add_list.get(1).equals("")) {
                a_current.add(add_list.get(2));
                a_avg = (int) add_list.get(3);
                a_med = (int) add_list.get(4);
                a_min = (int) add_list.get(5);
                a_min_iteration = (int) add_list.get(8);
                a_max = (int) add_list.get(6);
                a_max_iteration = (int) add_list.get(9);
                a_size = (int) add_list.get(7);

                purge_list = AMS.request_perf(ams_ip, mac, Operation.purge, i);
                print_preliminary_results(purge_list);
                if (purge_list.get(1).equals("")) {
                    p_current.add(purge_list.get(2));
                    p_avg = (int) purge_list.get(3);
                    p_med = (int) purge_list.get(4);
                    p_min = (int) purge_list.get(5);
                    p_min_iteration = (int) purge_list.get(8);
                    p_max = (int) purge_list.get(6);
                    p_max_iteration = (int) purge_list.get(9);
                    p_size = (int) purge_list.get(7);
                }
            }
            add_list.clear();
            purge_list.clear();
            Thread.sleep(sleep_after_iteration);
        }

        prepare_total_results(mac, boxname, count_reminders, count_iterations,
                a_avg, a_med, a_min, a_min_iteration, a_max, a_max_iteration, a_size, a_current,
                0, 0, 0, 0, 0, 0, 0, null,
                d_avg, d_med, d_min, d_min_iteration, d_max, d_max_iteration, d_size, d_current,
                p_avg, p_med, p_min, p_min_iteration, p_max, p_max_iteration, p_size, p_current);
        assertNotEquals(0, a_avg, "a_avg");
        assertNotEquals(0, p_avg, "p_avg");
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/reminders_oldapi.csv", numLinesToSkip = 1)
    void test12_Add_Delete_Purge(String ams_ip, String mac, String boxname, int count_reminders, int reminderChannelNumber, int reminderOffset, int reminderOffset_new, int count_iterations) throws IOException, InterruptedException {
        print_start_header(ams_ip, mac, boxname, count_reminders, reminderChannelNumber, reminderOffset, reminderOffset_new, count_iterations);

        for (int i = 1; i <= count_iterations; i++) {
            print_iteration_header(ams_ip, mac, count_reminders, i, count_iterations);

            add_list = AMS.request_perf(ams_ip, mac, Operation.add, i, count_reminders, reminderChannelNumber, reminderProgramStart, reminderProgramId, reminderOffset);
            print_preliminary_results(add_list);
            //assertEquals(expected200, add_list.get(0));
            //assertEquals("", add_list.get(1));
            if (add_list.get(0).equals(expected200) && add_list.get(1).equals("")) {
                a_current.add(add_list.get(2));
                a_avg = (int) add_list.get(3);
                a_med = (int) add_list.get(4);
                a_min = (int) add_list.get(5);
                a_min_iteration = (int) add_list.get(6);
                a_max = (int) add_list.get(7);
                a_max_iteration = (int) add_list.get(8);
                a_size = (int) add_list.get(9);

                delete_list = AMS.request_perf(ams_ip, mac, Operation.delete, i, count_reminders, reminderChannelNumber, reminderProgramStart, reminderProgramId, reminderOffset);
                print_preliminary_results(delete_list);
                if (delete_list.get(1).equals("")) {
                    d_current.add(delete_list.get(2));
                    d_avg = (int) delete_list.get(3);
                    d_med = (int) delete_list.get(4);
                    d_min = (int) delete_list.get(5);
                    d_min_iteration = (int) delete_list.get(6);
                    d_max = (int) delete_list.get(7);
                    d_max_iteration = (int) delete_list.get(8);
                    d_size = (int) delete_list.get(9);
                }

                purge_list = AMS.request_perf(ams_ip, mac, Operation.purge, i);
                print_preliminary_results(purge_list);
                if (purge_list.get(1).equals("")) {
                    p_current.add(purge_list.get(2));
                    p_avg = (int) purge_list.get(3);
                    p_med = (int) purge_list.get(4);
                    p_min = (int) purge_list.get(5);
                    p_min_iteration = (int) purge_list.get(6);
                    p_max = (int) purge_list.get(7);
                    p_max_iteration = (int) purge_list.get(8);
                    p_size = (int) purge_list.get(9);
                }
            }
            add_list.clear();
            delete_list.clear();
            purge_list.clear();
            Thread.sleep(sleep_after_iteration);
        }

        prepare_total_results(mac, boxname, count_reminders, count_iterations,
                a_avg, a_med, a_min, a_min_iteration, a_max, a_max_iteration, a_size, a_current,
                0, 0, 0, 0, 0, 0, 0, null,
                d_avg, d_med, d_min, d_min_iteration, d_max, d_max_iteration, d_size, d_current,
                p_avg, p_med, p_min, p_min_iteration, p_max, p_max_iteration, p_size, p_current);
        assertNotEquals(0, a_avg, "a_avg");
        assertNotEquals(0, d_avg, "d_avg");
        assertNotEquals(0, p_avg, "p_avg");
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/reminders_oldapi.csv", numLinesToSkip = 1)
    void test19_Purge(String ams_ip, String mac, String boxname, int count_reminders, int reminderChannelNumber, int reminderOffset, int reminderOffset_new, int count_iterations) throws IOException, InterruptedException {
        print_start_header(ams_ip, mac, boxname, count_reminders, reminderChannelNumber, reminderOffset, reminderOffset_new, count_iterations);

        for (int i = 1; i <= count_iterations; i++) {
            print_iteration_header(ams_ip, mac, count_reminders, i, count_iterations);

            purge_list = AMS.request_perf(ams_ip, mac, Operation.purge, i);
            print_preliminary_results(purge_list);
            if (purge_list.get(1).equals("")) {
                p_current.add(purge_list.get(2));
                p_avg = (int) purge_list.get(3);
                p_med = (int) purge_list.get(4);
                p_min = (int) purge_list.get(5);
                p_min_iteration = (int) purge_list.get(6);
                p_max = (int) purge_list.get(7);
                p_max_iteration = (int) purge_list.get(8);
                p_size = (int) purge_list.get(9);
            }

            purge_list.clear();
            Thread.sleep(sleep_after_iteration);
        }

        prepare_total_results(mac, boxname, count_reminders, count_iterations,
                a_avg, a_med, a_min, a_min_iteration, a_max, a_max_iteration, a_size, a_current,
                0, 0, 0, 0, 0, 0, 0, null,
                d_avg, d_med, d_min, d_min_iteration, d_max, d_max_iteration, d_size, d_current,
                p_avg, p_med, p_min, p_min_iteration, p_max, p_max_iteration, p_size, p_current);
        assertNotEquals(0, p_avg, "p_avg");
    }

}
