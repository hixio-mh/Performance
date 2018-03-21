import org.junit.Assert;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class testSandbox extends API {

    @Test
    @Disabled
    void testDate() {
        //String[] xxx = {};
        assertEquals("2018-03-16", get_date(1, false));
        assertEquals("2018-03-16 2018-03-17", get_date(2, true));
        assertEquals("2018-03-17", get_date(2, false));
        assertEquals("2018-03-16 2018-03-17", get_date(2, true));
    }

    @Test
    @Disabled
    void testTime() {
        assertEquals("02:30", get_time(150, false));
    }

    String get_date(int count, Boolean several) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat pattern = new SimpleDateFormat("yyyy-MM-dd");

        String result = "";
        String[] result2 = new String[count];

        if (several) {
            for (int i = 1; i <= count; i++) {
                calendar.add(Calendar.DAY_OF_YEAR, +1);
                //result += pattern.format(calendar.getTime());
                //rack_date = pattern.format(calendar.getTime());
                //System.out.println(rack_date[i]);
                result += pattern.format(calendar.getTime());
                if (i != count) {
                    result += " ";
                }
            }
        } else {
            calendar.add(Calendar.DAY_OF_YEAR, +count);
            result = pattern.format(calendar.getTime());
        }
        System.out.println("generated result: " + result);
        return result;
    }

    String get_time(int count, Boolean several) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat pattern = new SimpleDateFormat("HH:mm");
        calendar.setTime(new Date(0, 0, 0, 0, 0));

        calendar.add(Calendar.MINUTE, count);
        String result = pattern.format(calendar.getTime());
        System.out.println("generated times: " + result);
        return result;
    }

    @Test
    void testOldAPI_negative_Blablabla_400_Bad_Request() throws IOException, InterruptedException {
        long start = System.currentTimeMillis();
        ArrayList actual = api.Operation(ams_ip, macaddress[0], "Blablabla", false, 0, "", 0, "", 0, 0, 0);
        long finish = System.currentTimeMillis();
        System.out.println("[DBG] " + (finish - start) + "ms test, return code: " + actual);
        assertEquals(expected400, actual.get(0));
        assertEquals(expected400t, actual.get(1));
        assertEquals("", actual.get(2));
    }

    @Test
    void testNewAPI_negative_Blablabla_400_Bad_Request() throws IOException, InterruptedException {
        long start = System.currentTimeMillis();
        ArrayList actual = api.Operation(ams_ip, macaddress[0], "Blablabla", true, 0, "", 0, "", 0, 0, 0);
        long finish = System.currentTimeMillis();
        System.out.println("[DBG] " + (finish - start) + "ms test, return code: " + actual);
        assertEquals(expected400, actual.get(0));
        assertEquals(expected400t, actual.get(1));
        assertEquals("", actual.get(2));
    }

    @Test
    void testOracleDB_Query() throws SQLException, ClassNotFoundException {
        long start = System.currentTimeMillis();
        ArrayList result = api.QueryDB("172.30.81.4", macaddress[0]);
        long finish = System.currentTimeMillis();
        System.out.println("[DBG] " + (finish - start) + "ms test, return result: "
                + result.get(0) + "  "
                + result.get(1) + "  "
                + result.get(2) + "  "
                + result.get(3) + "  "
                + result.get(4));
        assertNotEquals(0, result.get(0));
        assertNotEquals(0, result.get(1));
        assertNotEquals(0, result.get(2));
        assertNotEquals(0, result.get(3));
        assertNotEquals(0, result.get(4));
    }

    @Test
    @Disabled
    void testCheck_registration_No_amsIp_found_for_macAddress() throws IOException {
        long start = System.currentTimeMillis();
        ArrayList actual = api.Check_registration("343834383438", charterapi_);
        long finish = System.currentTimeMillis();
        System.out.println("[DBG] " + (finish - start) + "ms test, return code: " + actual);
        Assert.assertEquals(expected500, actual.get(0));
        Assert.assertEquals(expected500t, actual.get(1));
        Assert.assertEquals("No amsIp found for macAddress", actual.get(2));
    }

    @org.junit.Test(timeout = 20000)
    void testCheck_registration_b_No_amsIp_found_for_macAddress() throws IOException {
        long start = System.currentTimeMillis();
        ArrayList actual = api.Check_registration("343834383438", charterapi_b);
        long finish = System.currentTimeMillis();
        System.out.println("[DBG] " + (finish - start) + "ms test, return code: " + actual);
        Assert.assertEquals(expected500, actual.get(0));
        Assert.assertEquals(expected500t, actual.get(1));
        Assert.assertEquals("No amsIp found for macAddress", actual.get(2));
    }

    @Test
    void testCheck_registration_c_No_amsIp_found_for_macAddress() throws IOException {
        long start = System.currentTimeMillis();
        ArrayList actual = api.Check_registration("123456789012", charterapi_c);
        long finish = System.currentTimeMillis();
        System.out.println("[DBG] " + (finish - start) + "ms test, return code: " + actual);
        Assert.assertEquals(expected500, actual.get(0));
        Assert.assertEquals(expected500t, actual.get(1));
        Assert.assertEquals("No amsIp found for macAddress", actual.get(2));
    }

    @Test
    void testCheck_registration_d_No_amsIp_found_for_macAddress() throws IOException {
        long start = System.currentTimeMillis();
        ArrayList actual = api.Check_registration("343834383438", charterapi_d);
        long finish = System.currentTimeMillis();
        System.out.println("[DBG] " + (finish - start) + "ms test, return code: " + actual);
        Assert.assertEquals(expected500, actual.get(0));
        Assert.assertEquals(expected500t, actual.get(1));
        Assert.assertEquals("No amsIp found for macAddress", actual.get(2));
    }

    @org.junit.Test(timeout = 20000)
    public void testChange_registration_to_invalid_ams127_0_0_1() throws IOException, InterruptedException {
        long start = System.currentTimeMillis();
        ArrayList actual = api.Change_registration(macaddress[0], charterapi, "127.0.0.1");
        long finish = System.currentTimeMillis();
        System.out.println("[DBG] " + (finish - start) + "ms test, return code: " + actual);
        Assert.assertEquals(expected200, actual.get(0));
        Assert.assertEquals(expected200t, actual.get(1));
        Assert.assertEquals("SUCCESS", actual.get(2));
    }

    @Test
    void test_OldAPI_negative_REM_ST_001_Box_is_not_registered() throws IOException, InterruptedException {
        long start = System.currentTimeMillis();
        ArrayList actual = api.Operation("172.30.81.0", macaddress[0], "Add", false, 2,
                reminderProgramStart, reminderChannelNumber, reminderProgramId,
                reminderOffset, reminderScheduleId, reminderId);
        long finish = System.currentTimeMillis();
        System.out.println("[DBG] " + (finish - start) + "ms test, " + "return code: " + actual);
        assertEquals(expected500, actual.get(0));
        assertEquals(expected500t, actual.get(1));
        assertEquals("REM-ST-001 Box is not registered", actual.get(2));
    }

    @Test
    void test_NewAPI_negative_REM_ST_001_Box_is_not_registered() throws IOException, InterruptedException {
        long start = System.currentTimeMillis();
        ArrayList actual = api.Operation("172.30.81.0", macaddress[0], "Add", true, 1,
                reminderProgramStart, reminderChannelNumber, reminderProgramId,
                reminderOffset, reminderScheduleId, reminderId);
        long finish = System.currentTimeMillis();
        System.out.println("[DBG] " + (finish - start) + "ms test, " + "return code: " + actual);
        assertEquals(expected500, actual.get(0));
        assertEquals(expected500t, actual.get(1));
        assertEquals("REM-ST-001 Box is not registered", actual.get(2));
    }

    @Test
    void test_NewAPI_Modify_negative_REM_ST_001_Box_is_not_registered() throws IOException, InterruptedException {
        long start = System.currentTimeMillis();
        ArrayList actual = api.Operation(ams_ip, macaddress[0], "Modify", true, 1,
                reminderProgramStart, reminderChannelNumber, reminderProgramId,
                reminderOffset, reminderScheduleId, reminderId);
        long finish = System.currentTimeMillis();
        System.out.println("[DBG] " + (finish - start) + "ms test, " + "return code: " + actual);
        assertEquals(expected500, actual.get(0));
        assertEquals(expected500t, actual.get(1));
        assertEquals("REM-ST-001 Box is not registered", actual.get(2));
    }

}