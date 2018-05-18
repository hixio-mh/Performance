package tv.zodiac.dev;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class testSandbox extends API {

    private NEWAPI_AMS AMS = new NEWAPI_AMS();

    @Test
    void testDate() {
        assertEquals("2018-03-31", reminderProgramStart);
        assertEquals("2018-03-31", get_date(1, false));
        assertEquals("2018-03-31 2018-04-01", get_date(2, true));
        assertEquals("2018-04-01", get_date(2, false));
    }

    @Test
    void testTime() {
        //assertEquals("00:30", get_time(1, 2));
        Random random = new Random();
        System.out.println(random.nextLong());

        Random random2 = new Random();
        //random.setSeed(1);
        System.out.println(random2.nextLong());
    }

    @Test
    void test_get_time2() {
        int count_reminders = 1000;
        for (int i=1; i<=count_reminders; i++) {
            System.out.print(get_time2(i) + ", ");
        }
    }

    @Test
    void testOperation_NewAPI_400_Bad_request() throws IOException {
        ArrayList actual = AMS.request(ams_ip, mac, Operation.blablabla, count_reminders, reminderProgramStart, reminderChannelNumber, reminderProgramId, reminderOffset, reminderScheduleId, reminderId);
        assertEquals(expected400, actual.get(0));
        assertEquals("", actual.get(1));
    }

    @Test
    void testOracleDB_Query() throws SQLException, ClassNotFoundException {
        ArrayList actual = AMS.QueryDB(ams_ip, mac);
        assertFalse(actual.isEmpty());

        assertEquals(Long.class, actual.get(0).getClass());
        assertNotEquals(0, actual.get(0));

        assertEquals(Long.class, actual.get(1).getClass());
        assertNotEquals(0, actual.get(1));

        assertEquals(Integer.class, actual.get(2).getClass());
        assertEquals(0, actual.get(2));

        assertEquals(String.class, actual.get(3).getClass());
        assertNotEquals(0, actual.get(3));

        assertEquals(String.class, actual.get(4).getClass());
        assertNotEquals(0, actual.get(4));
    }

    @Test
    void testOracleDB_Query_macaddress_empty() throws SQLException, ClassNotFoundException {
        ArrayList result = AMS.QueryDB(ams_ip, "");
        assertTrue(result.isEmpty());
    }

    @Test
    void testOracleDB_Query_macaddress_wrong() throws SQLException, ClassNotFoundException {
        ArrayList result = AMS.QueryDB(ams_ip, mac_wrong);
        assertTrue(result.isEmpty());
    }

    @Test
    void testCheck_Delete() throws IOException {
        ArrayList actual = AMS.request(ams_ip, mac, Operation.delete, count_reminders, reminderProgramStart, reminderChannelNumber, reminderProgramId, reminderOffset, reminderScheduleId, reminderId);
        assertEquals(expected200, actual.get(0));
        assertEquals("", actual.get(1));
    }



}
