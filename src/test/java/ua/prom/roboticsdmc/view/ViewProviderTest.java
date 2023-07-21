package ua.prom.roboticsdmc.view;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ViewProviderTest")

class ViewProviderTest {
    
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    @DisplayName("printMessage() print String when entered only one input")
    void printMessage_shouldPrintString_whenIsOnlyOneInput() {

        String message = "Message";
        Scanner scanner = new Scanner(System.in);
        ViewProvider viewProvider = new ViewProvider(scanner);
        
        viewProvider.printMessage(message);

        Assert.assertEquals("Message", outputStreamCaptor.toString().trim());
    }
    
    @Test
    @DisplayName("printMessage() print formated String when there are three inputs: Strind and two separate int")
    void printMessage_shouldPrintFormatedString_whenThereAreThreeInputsStringAndTwoSeparteInt() {

        String message = "Message";
        int rowOffset = 5;
        int rowLimit = 2;
        String expected = "Message (6 - 7):";
        Scanner scanner = new Scanner(System.in);
        ViewProvider viewProvider = new ViewProvider(scanner);
        
        viewProvider.printMessage(message, rowOffset, rowLimit);

        Assert.assertEquals(expected, outputStreamCaptor.toString().trim());
    }
    
    @Test
    @DisplayName("read() should return String when Console input is something correct")
    void read_shouldReturnString_whenConsoleInputIsSomethingCorrect() {

        String input = "Console message";
        String expected = "Console message";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        Scanner scanner = new Scanner(System.in);
        ViewProvider viewProvider = new ViewProvider(scanner);

        assertEquals(expected, viewProvider.read());
    }
    
    @Test
    @DisplayName("readInt() should returnt int when console input is int")
    void readInt_shouldReturnInt_whenConsoleInputIsInt() {
        
        String input = "12";
        int expected = 12;
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        Scanner scanner = new Scanner(System.in);
        ViewProvider viewProvider = new ViewProvider(scanner);
        
        assertEquals(expected, viewProvider.readInt());
    } 
}
