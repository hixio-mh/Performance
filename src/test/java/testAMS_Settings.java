import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * We are as Middle: chain of requests: localhost -> AMS -> box -> AMS -> localhost (Middle)
 */
public class testAMS_Settings extends API {

    private API_AMS AMS = new API_AMS();

    @Test
    @Ignore
    public void testSettings_Channel_Filters() throws IOException {
        //String json = "{"settings":{"groups":
        // [{"id":"STB000005FE680A",
        // "type":"device-stb",
        // "options": [{"name":"Channel Filters","value":["Premiums"]}]
        // }]
        // }}"
        ArrayList actual = AMS.Change_settings(macaddress, "Channel Filters", "Premiums");
        assertEquals(expected200, actual.get(0));
        assertEquals("", actual.get(1));
    }

    @Test
    public void testSettings_Audio_Output_to_Other() throws IOException {
        ArrayList actual = AMS.Change_settings(macaddress, "Audio Output", "Other");
        assertEquals(expected200, actual.get(0));
        assertEquals("", actual.get(1));
    }

    @Test
    public void testSettings_Audio_Output_to_Dolby_Digital() throws IOException {
        ArrayList actual = AMS.Change_settings(macaddress, "Audio Output", "Dolby Digital");
        assertEquals(expected200, actual.get(0));
        assertEquals("", actual.get(1));
    }

    @Test
    public void testSettings_Audio_Output_to_HDMI() throws IOException {
        ArrayList actual = AMS.Change_settings(macaddress, "Audio Output", "HDMI");
        assertEquals(expected200, actual.get(0));
        assertEquals("", actual.get(1));
    }

    @Test
    public void testSettings_negative_STB_MAC_not_found() throws IOException {
        ArrayList actual = AMS.Change_settings(macaddress_wrong, "Audio Output", "HDMI");
        assertEquals(expected404, actual.get(0));
        assertEquals("STB MAC not found: " + macaddress_wrong, actual.get(1));
    }

    @Test
    public void testSettings_Audio_Output_to_value_empty() throws IOException {
        ArrayList actual = AMS.Change_settings(macaddress, "Audio Output", "");
        assertEquals(expected200, actual.get(0));
        assertEquals("incorrect value", actual.get(1));
    }

    @Test
    public void testSettings_Audio_Output_to_value_wrong() throws IOException {
        ArrayList actual = AMS.Change_settings(macaddress, "Audio Output", "blablabla");
        assertEquals(expected200, actual.get(0));
        assertEquals("incorrect value", actual.get(1));
    }

    @Test
    public void testSettings_negative_SET_025_Unsupported_data_type_Not_a_JSON_Object() throws IOException {
        ArrayList actual = AMS.Change_settings(macaddress, "Audio Output", "");
        assertEquals(expected500, actual.get(0));
        assertEquals("SET-025 Unsupported data type: Not a JSON Object:", actual.get(1));
    }

    @Test
    public void testSettings_Turn_Reminders_to_Off() throws IOException {
        ArrayList actual = AMS.Change_settings(macaddress, "Turn On/Off Reminders", "Off");
        assertEquals(expected200, actual.get(0));
        assertEquals("", actual.get(1));
    }

    @Test
    public void testSettings_Turn_Reminders_to_On() throws IOException {
        ArrayList actual = AMS.Change_settings(macaddress, "Turn On/Off Reminders", "On");
        assertEquals(expected200, actual.get(0));
        assertEquals("", actual.get(1));
    }

    @Test
    public void testSettings_Turn_Reminders_to_empty_value() throws IOException {
        ArrayList actual = AMS.Change_settings(macaddress, "Turn On/Off Reminders", "");
        assertEquals(expected200, actual.get(0));
        assertEquals("incorrect value", actual.get(1));
    }

    @Test
    public void testSettings_Guide_Narration_to_On() throws IOException {
        ArrayList actual = AMS.Change_settings(macaddress, "Guide Narration", "On");
        assertEquals(expected200, actual.get(0));
        assertEquals("", actual.get(1));
    }

    @Test
    public void testSettings_Guide_Narration_to_Off() throws IOException {
        ArrayList actual = AMS.Change_settings(macaddress, "Guide Narration", "Off");
        assertEquals(expected200, actual.get(0));
        assertEquals("", actual.get(1));
    }

    @Test
    public void testSettings_PC_to_On() throws IOException {
        ArrayList actual = AMS.Change_settings(macaddress, "Enable/Disable Parental Controls", "On");
        assertEquals(expected200, actual.get(0));
        assertEquals("", actual.get(1));
    }

    @Test
    public void testSettings_PC_to_Off() throws IOException {
        ArrayList actual = AMS.Change_settings(macaddress, "Enable/Disable Parental Controls", "Off");
        assertEquals(expected200, actual.get(0));
        assertEquals("", actual.get(1));
    }

}