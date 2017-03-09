/*
   Copyright 2017 Remko Popma

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package picocli;

import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static java.util.concurrent.TimeUnit.*;
import static org.junit.Assert.*;
import static picocli.CommandLine.*;

/**
 * Tests for the CommandLine argument parsing interpreter functionality.
 */
// DONE arrays
// DONE collection fields
// DONE all built-in types
// DONE private fields, public fields (TODO methods?)
// DONE arity 2, 3
// DONE arity -1, -2, -3
// TODO arity ignored for single-value types (non-array, non-collection)
// DONE positional arguments with '--' separator (where positional arguments look like options)
// DONE positional arguments without '--' separator (based on arity of last option?)
// DONE positional arguments based on arity of last option
// TODO ambiguous options: writing --input ARG (as opposed to --input=ARG) is ambiguous,
// meaning it is not possible to tell whether ARG is option's argument or a positional argument.
// In usage patterns this will be interpreted as an option with argument only if a description (covered below)
// for that option is provided.
// Otherwise it will be interpreted as an option and separate positional argument.
// TODO ambiguous short options: ambiguity with the -f FILE and -fFILE notation.
// In the latter case it is not possible to tell whether it is a number of stacked short options,
// or an option with an argument. These notations will be interpreted as an option with argument only if a
// description for the option is provided.
// DONE compact flags
// DONE compact flags where last option has an argument, separate by space
// DONE compact flags where last option has an argument attached to the option character
// DONE long options with argument separate by space
// DONE long options with argument separated by '=' (no spaces)
// TODO document that if arity>1 and args="-opt=val1 val2", arity overrules the "=": both values are assigned
// TODO test superclass bean and child class bean where child class field shadows super class and have same annotation Parameter name
// TODO test superclass bean and child class bean where child class field shadows super class and have different annotation Parameter name
// DONE -vrx, -vro outputFile, -vrooutputFile, -vro=outputFile, -vro:outputFile, -vro=, -vro:, -vro
// DONE --out outputFile, --out=outputFile, --out:outputFile, --out=, --out:, --out
public class CommandLineTest {
    @Test
    public void testVersion() {
        assertEquals("0.2.0", CommandLine.VERSION);
    }

    private static class SupportedTypes {
        @CommandLine.Parameter(names = "-boolean")       boolean booleanField;
        @CommandLine.Parameter(names = "-Boolean")       Boolean aBooleanField;
        @CommandLine.Parameter(names = "-byte")          byte byteField;
        @CommandLine.Parameter(names = "-Byte")          Byte aByteField;
        @CommandLine.Parameter(names = "-char")          char charField;
        @CommandLine.Parameter(names = "-Character")     Character aCharacterField;
        @CommandLine.Parameter(names = "-short")         short shortField;
        @CommandLine.Parameter(names = "-Short")         Short aShortField;
        @CommandLine.Parameter(names = "-int")           int intField;
        @CommandLine.Parameter(names = "-Integer")       Integer anIntegerField;
        @CommandLine.Parameter(names = "-long")          long longField;
        @CommandLine.Parameter(names = "-Long")          Long aLongField;
        @CommandLine.Parameter(names = "-float")         float floatField;
        @CommandLine.Parameter(names = "-Float")         Float aFloatField;
        @CommandLine.Parameter(names = "-double")        double doubleField;
        @CommandLine.Parameter(names = "-Double")        Double aDoubleField;
        @CommandLine.Parameter(names = "-String")        String aStringField;
        @CommandLine.Parameter(names = "-StringBuilder") StringBuilder aStringBuilderField;
        @CommandLine.Parameter(names = "-CharSequence")  CharSequence aCharSequenceField;
        @CommandLine.Parameter(names = "-File")          File aFileField;
        @CommandLine.Parameter(names = "-URL")           URL anURLField;
        @CommandLine.Parameter(names = "-URI")           URI anURIField;
        @CommandLine.Parameter(names = "-Date")          Date aDateField;
        @CommandLine.Parameter(names = "-Time")          Time aTimeField;
        @CommandLine.Parameter(names = "-BigDecimal")    BigDecimal aBigDecimalField;
        @CommandLine.Parameter(names = "-BigInteger")    BigInteger aBigIntegerField;
        @CommandLine.Parameter(names = "-Charset")       Charset aCharsetField;
        @CommandLine.Parameter(names = "-InetAddress")   InetAddress anInetAddressField;
        @CommandLine.Parameter(names = "-Pattern")       Pattern aPatternField;
        @CommandLine.Parameter(names = "-UUID")          UUID anUUIDField;
    }
    @Test
    public void testDefaults() {
        SupportedTypes bean = CommandLine.parse(new SupportedTypes());
        assertEquals("boolean", false, bean.booleanField);
        assertEquals("Boolean", null, bean.aBooleanField);
        assertEquals("byte", 0, bean.byteField);
        assertEquals("Byte", null, bean.aByteField);
        assertEquals("char", 0, bean.charField);
        assertEquals("Character", null, bean.aCharacterField);
        assertEquals("short", 0, bean.shortField);
        assertEquals("Short", null, bean.aShortField);
        assertEquals("int", 0, bean.intField);
        assertEquals("Integer", null, bean.anIntegerField);
        assertEquals("long", 0, bean.longField);
        assertEquals("Long", null, bean.aLongField);
        assertEquals("float", 0f, bean.floatField, Float.MIN_VALUE);
        assertEquals("Float", null, bean.aFloatField);
        assertEquals("double", 0d, bean.doubleField, Double.MIN_VALUE);
        assertEquals("Double", null, bean.aDoubleField);
        assertEquals("String", null, bean.aStringField);
        assertEquals("StringBuilder", null, bean.aStringBuilderField);
        assertEquals("CharSequence", null, bean.aCharSequenceField);
        assertEquals("File", null, bean.aFileField);
        assertEquals("URL", null, bean.anURLField);
        assertEquals("URI", null, bean.anURIField);
        assertEquals("Date", null, bean.aDateField);
        assertEquals("Time", null, bean.aTimeField);
        assertEquals("BigDecimal", null, bean.aBigDecimalField);
        assertEquals("BigInteger", null, bean.aBigIntegerField);
        assertEquals("Charset", null, bean.aCharsetField);
        assertEquals("InetAddress", null, bean.anInetAddressField);
        assertEquals("Pattern", null, bean.aPatternField);
        assertEquals("UUID", null, bean.anUUIDField);
    }
    @Test
    public void testTypeConversionSucceedsForValidInput()
            throws MalformedURLException, URISyntaxException, UnknownHostException, ParseException {
        SupportedTypes bean = CommandLine.parse(new SupportedTypes(),
                "-boolean", "-Boolean", //
                "-byte", "12", "-Byte", "23", //
                "-char", "p", "-Character", "i", //
                "-short", "34", "-Short", "45", //
                "-int", "56", "-Integer", "67", //
                "-long", "78", "-Long", "89", //
                "-float", "1.23", "-Float", "2.34", //
                "-double", "3.45", "-Double", "4.56", //
                "-String", "abc", "-StringBuilder", "bcd", "-CharSequence", "xyz", //
                "-File", "abc.txt", //
                "-URL", "http://pico-cli.github.io", //
                "-URI", "http://pico-cli.github.io/index.html", //
                "-Date", "2017-01-30", //
                "-Time", "23:59:59", //
                "-BigDecimal", "12345678901234567890.123", //
                "-BigInteger", "123456789012345678901", //
                "-Charset", "UTF8", //
                "-InetAddress", InetAddress.getLocalHost().getHostName(), //
                "-Pattern", "a*b", //
                "-UUID", "c7d51423-bf9d-45dd-a30d-5b16fafe42e2"
        );
        assertEquals("boolean", true, bean.booleanField);
        assertEquals("Boolean", Boolean.TRUE, bean.aBooleanField);
        assertEquals("byte", 12, bean.byteField);
        assertEquals("Byte", Byte.valueOf((byte) 23), bean.aByteField);
        assertEquals("char", 'p', bean.charField);
        assertEquals("Character", Character.valueOf('i'), bean.aCharacterField);
        assertEquals("short", 34, bean.shortField);
        assertEquals("Short", Short.valueOf((short) 45), bean.aShortField);
        assertEquals("int", 56, bean.intField);
        assertEquals("Integer", Integer.valueOf(67), bean.anIntegerField);
        assertEquals("long", 78L, bean.longField);
        assertEquals("Long", Long.valueOf(89L), bean.aLongField);
        assertEquals("float", 1.23f, bean.floatField, Float.MIN_VALUE);
        assertEquals("Float", Float.valueOf(2.34f), bean.aFloatField);
        assertEquals("double", 3.45, bean.doubleField, Double.MIN_VALUE);
        assertEquals("Double", Double.valueOf(4.56), bean.aDoubleField);
        assertEquals("String", "abc", bean.aStringField);
        assertEquals("StringBuilder type", StringBuilder.class, bean.aStringBuilderField.getClass());
        assertEquals("StringBuilder", "bcd", bean.aStringBuilderField.toString());
        assertEquals("CharSequence", "xyz", bean.aCharSequenceField);
        assertEquals("File", new File("abc.txt"), bean.aFileField);
        assertEquals("URL", new URL("http://pico-cli.github.io"), bean.anURLField);
        assertEquals("URI", new URI("http://pico-cli.github.io/index.html"), bean.anURIField);
        assertEquals("Date", new SimpleDateFormat("yyyy-MM-dd").parse("2017-01-30"), bean.aDateField);
        assertEquals("Time", new Time(new SimpleDateFormat("HH:mm:ss").parse("23:59:59").getTime()), bean.aTimeField);
        assertEquals("BigDecimal", new BigDecimal("12345678901234567890.123"), bean.aBigDecimalField);
        assertEquals("BigInteger", new BigInteger("123456789012345678901"), bean.aBigIntegerField);
        assertEquals("Charset", Charset.forName("UTF8"), bean.aCharsetField);
        assertEquals("InetAddress", InetAddress.getByName(InetAddress.getLocalHost().getHostName()), bean.anInetAddressField);
        assertEquals("Pattern", Pattern.compile("a*b").pattern(), bean.aPatternField.pattern());
        assertEquals("UUID", UUID.fromString("c7d51423-bf9d-45dd-a30d-5b16fafe42e2"), bean.anUUIDField);
    }
    @Test
    public void testByteFieldsCanBeSpecifiedInHexadecimal() {
        SupportedTypes bean = CommandLine.parse(new SupportedTypes(), "-byte", "0x1F", "-Byte", "0x0F");
        assertEquals(0x1F, bean.byteField);
        assertEquals(Byte.valueOf((byte) 0x0F), bean.aByteField);
    }
    @Test
    public void testShortFieldsCanBeSpecifiedInHexadecimal() {
        SupportedTypes bean = CommandLine.parse(new SupportedTypes(), "-short", "0xFF", "-Short", "0x6FFE");
        assertEquals(255, bean.shortField);
        assertEquals(Short.valueOf((short) 0x6FFE), bean.aShortField);
    }
    @Test
    public void testIntFieldsCanBeSpecifiedInHexadecimal() {
        SupportedTypes bean = CommandLine.parse(new SupportedTypes(), "-int", "0xFF", "-Integer", "0xFFFF");
        assertEquals(255, bean.intField);
        assertEquals(Integer.valueOf(0xFFFF), bean.anIntegerField);
    }
    @Test
    public void testLongFieldsCanBeSpecifiedInHexadecimal() {
        SupportedTypes bean = CommandLine.parse(new SupportedTypes(), "-long", "0xAABBCC", "-Long", "0xAABBCCDD");
        assertEquals(0xAABBCC, bean.longField);
        assertEquals(Long.valueOf(0xAABBCCDDL), bean.aLongField);
    }
    @Test
    public void testByteFieldsCanBeSpecifiedInOctal() {
        SupportedTypes bean = CommandLine.parse(new SupportedTypes(), "-byte", "010", "-Byte", "010");
        assertEquals(8, bean.byteField);
        assertEquals(Byte.valueOf((byte) 8), bean.aByteField);
    }
    @Test
    public void testShortFieldsCanBeSpecifiedInOctal() {
        SupportedTypes bean = CommandLine.parse(new SupportedTypes(), "-short", "010", "-Short", "010");
        assertEquals(8, bean.shortField);
        assertEquals(Short.valueOf((short) 8), bean.aShortField);
    }
    @Test
    public void testIntFieldsCanBeSpecifiedInOctal() {
        SupportedTypes bean = CommandLine.parse(new SupportedTypes(), "-int", "010", "-Integer", "010");
        assertEquals(8, bean.intField);
        assertEquals(Integer.valueOf(8), bean.anIntegerField);
    }
    @Test
    public void testLongFieldsCanBeSpecifiedInOctal() {
        SupportedTypes bean = CommandLine.parse(new SupportedTypes(), "-long", "010", "-Long", "010");
        assertEquals(8, bean.longField);
        assertEquals(Long.valueOf(8), bean.aLongField);
    }
    @Test(expected = MissingParameterException.class)
    public void testSingleValueFieldDefaultMinArityIs1() {
        CommandLine.parse(new SupportedTypes(),  "-Long");
    }
    @Test
    public void testSingleValueFieldDefaultMinArityIsOne() {
        try {
            CommandLine.parse(new SupportedTypes(),  "-Long", "-boolean");
            fail("should fail");
        } catch (ParameterException ex) {
            assertEquals("Could not convert '-boolean' to Long for option '-Long'", ex.getMessage());
        }
    }
    @Test
    public void testTimeFormatHHmmSupported() throws ParseException {
        SupportedTypes bean = CommandLine.parse(new SupportedTypes(), "-Time", "23:59");
        assertEquals("Time", new Time(new SimpleDateFormat("HH:mm").parse("23:59").getTime()), bean.aTimeField);
    }
    @Test
    public void testTimeFormatHHmmssSupported() throws ParseException {
        SupportedTypes bean = CommandLine.parse(new SupportedTypes(), "-Time", "23:59:58");
        assertEquals("Time", new Time(new SimpleDateFormat("HH:mm:ss").parse("23:59:58").getTime()), bean.aTimeField);
    }
    @Test
    public void testTimeFormatHHmmssDotSSSSupported() throws ParseException {
        SupportedTypes bean = CommandLine.parse(new SupportedTypes(), "-Time", "23:59:58.123");
        assertEquals("Time", new Time(new SimpleDateFormat("HH:mm:ss.SSS").parse("23:59:58.123").getTime()), bean.aTimeField);
    }
    @Test
    public void testTimeFormatHHmmssCommaSSSSupported() throws ParseException {
        SupportedTypes bean = CommandLine.parse(new SupportedTypes(), "-Time", "23:59:58,123");
        assertEquals("Time", new Time(new SimpleDateFormat("HH:mm:ss,SSS").parse("23:59:58,123").getTime()), bean.aTimeField);
    }
    @Test
    public void testTimeFormatHHmmssSSSInvalidError() throws ParseException {
        try {
            CommandLine.parse(new SupportedTypes(), "-Time", "23:59:58;123");
            fail("Invalid format was accepted");
        } catch (ParameterException expected) {
            assertEquals("'23:59:58;123' is not a HH:mm[:ss[.SSS]] time for option '-Time'", expected.getMessage());
        }
    }
    @Test
    public void testTimeFormatHHmmssDotInvalidError() throws ParseException {
        try {
            CommandLine.parse(new SupportedTypes(), "-Time", "23:59:58.");
            fail("Invalid format was accepted");
        } catch (ParameterException expected) {
            assertEquals("'23:59:58.' is not a HH:mm[:ss[.SSS]] time for option '-Time'", expected.getMessage());
        }
    }
    @Test
    public void testTimeFormatHHmmsssInvalidError() throws ParseException {
        try {
            CommandLine.parse(new SupportedTypes(), "-Time", "23:59:587");
            fail("Invalid format was accepted");
        } catch (ParameterException expected) {
            assertEquals("'23:59:587' is not a HH:mm[:ss[.SSS]] time for option '-Time'", expected.getMessage());
        }
    }
    @Test
    public void testTimeFormatHHmmssColonInvalidError() throws ParseException {
        try {
            CommandLine.parse(new SupportedTypes(), "-Time", "23:59:");
            fail("Invalid format was accepted");
        } catch (ParameterException expected) {
            assertEquals("'23:59:' is not a HH:mm[:ss[.SSS]] time for option '-Time'", expected.getMessage());
        }
    }
    @Test
    public void testDateFormatYYYYmmddInvalidError() throws ParseException {
        try {
            CommandLine.parse(new SupportedTypes(), "-Date", "20170131");
            fail("Invalid format was accepted");
        } catch (ParameterException expected) {
            assertEquals("'20170131' is not a yyyy-MM-dd date for option '-Date'", expected.getMessage());
        }
    }
    @Test
    public void testCharConverterInvalidError() throws ParseException {
        try {
            CommandLine.parse(new SupportedTypes(), "-Character", "aa");
            fail("Invalid format was accepted");
        } catch (ParameterException expected) {
            assertEquals("'aa' is not a single character for option '-Character'", expected.getMessage());
        }
        try {
            CommandLine.parse(new SupportedTypes(), "-char", "aa");
            fail("Invalid format was accepted");
        } catch (ParameterException expected) {
            assertEquals("'aa' is not a single character for option '-char'", expected.getMessage());
        }
    }
    @Test
    public void testNumberConvertersInvalidError() {
        parseInvalidValue("-Byte", "aa");
        parseInvalidValue("-byte", "aa");
        parseInvalidValue("-Short", "aa");
        parseInvalidValue("-short", "aa");
        parseInvalidValue("-Integer", "aa");
        parseInvalidValue("-int", "aa");
        parseInvalidValue("-Long", "aa");
        parseInvalidValue("-long", "aa");
        parseInvalidValue("-Float", "aa");
        parseInvalidValue("-float", "aa");
        parseInvalidValue("-Double", "aa");
        parseInvalidValue("-double", "aa");
        parseInvalidValue("-BigDecimal", "aa");
        parseInvalidValue("-BigInteger", "aa");
    }
    @Test
    public void testDomainObjectConvertersInvalidError() {
        parseInvalidValue("-URL", ":::");
        parseInvalidValue("-URI", ":::");
        parseInvalidValue("-Charset", "aa");
        parseInvalidValue("-InetAddress", "::a?*!a");
        parseInvalidValue("-Pattern", "[[(aa");
        parseInvalidValue("-UUID", "aa");
    }

    private void parseInvalidValue(String option, String value) {
        try {
            CommandLine.parse(new SupportedTypes(), option, value);
            fail("Invalid format " + value + " was accepted for " + option);
        } catch (ParameterException expected) {
            String type = option.substring(1);
            assertEquals("Could not convert '" + value + "' to " + type
                    + " for option '" + option + "'", expected.getMessage());
        }
    }

    static class EnumParams {
        @CommandLine.Parameter(names = "-timeUnit") TimeUnit timeUnit;
        @CommandLine.Parameter(names = "-timeUnitArray", arity = "2") TimeUnit[] timeUnitArray;
        @CommandLine.Parameter(names = "-timeUnitList", type = TimeUnit.class, arity = "3") List<TimeUnit> timeUnitList;
    }
    @Test
    public void testEnumTypeConversionSuceedsForValidInput() {
        EnumParams params = CommandLine.parse(new EnumParams(),
                "-timeUnit DAYS -timeUnitArray HOURS MINUTES -timeUnitList SECONDS MICROSECONDS NANOSECONDS".split(" "));
        assertEquals(DAYS, params.timeUnit);
        assertArrayEquals(new TimeUnit[]{HOURS, TimeUnit.MINUTES}, params.timeUnitArray);
        List<TimeUnit> expected = new ArrayList<TimeUnit>(Arrays.asList(TimeUnit.SECONDS, TimeUnit.MICROSECONDS, TimeUnit.NANOSECONDS));
        assertEquals(expected, params.timeUnitList);
    }
    @Test
    public void testEnumTypeConversionFailsForInvalidInput() {
        try {
            CommandLine.parse(new EnumParams(), "-timeUnit", "xyz");
            fail("Accepted invalid timeunit");
        } catch (Exception ex) {
            assertEquals("Could not convert 'xyz' to TimeUnit for option '-timeUnit'", ex.getMessage());
        }
    }
    @Ignore("Requires #14 case-insensitive enum parsing")
    @Test
    public void testEnumTypeConversionIsCaseInsensitive() {
        EnumParams params = CommandLine.parse(new EnumParams(),
                "-timeUnit daYS -timeUnitArray hours miNutEs -timeUnitList SEConds MiCROsEconds nanoSEConds".split(" "));
        assertEquals(DAYS, params.timeUnit);
        assertArrayEquals(new TimeUnit[]{HOURS, TimeUnit.MINUTES}, params.timeUnitArray);
        List<TimeUnit> expected = new ArrayList<TimeUnit>(Arrays.asList(TimeUnit.SECONDS, TimeUnit.MICROSECONDS, TimeUnit.NANOSECONDS));
        assertEquals(expected, params.timeUnitList);
    }
    @Test
    public void testEnumArrayTypeConversionFailsForInvalidInput() {
        try {
            CommandLine.parse(new EnumParams(), "-timeUnitArray", "a", "b");
            fail("Accepted invalid timeunit");
        } catch (Exception ex) {
            assertEquals("Could not convert 'a' to TimeUnit[] for option '-timeUnitArray' parameter[0]", ex.getMessage());
        }
    }
    @Test
    public void testEnumListTypeConversionFailsForInvalidInput() {
        try {
            CommandLine.parse(new EnumParams(), "-timeUnitList", "DAYS", "b", "c");
            fail("Accepted invalid timeunit");
        } catch (Exception ex) {
            assertEquals("Could not convert 'b' to TimeUnit for option '-timeUnitList' parameter[1]", ex.getMessage());
        }
    }

    @Test
    public void testArrayOptionParametersAreAlwaysInstantiated() {
        EnumParams params = new EnumParams();
        TimeUnit[] array = params.timeUnitArray;
        new CommandLine(params).parse("-timeUnitArray", "DAYS", "HOURS");
        assertNotSame(array, params.timeUnitArray);
    }
    @Test
    public void testListOptionParametersAreInstantiatedIfNull() {
        EnumParams params = new EnumParams();
        assertNull(params.timeUnitList);
        new CommandLine(params).parse("-timeUnitList", "DAYS", "HOURS", "DAYS");
        assertEquals(Arrays.asList(DAYS, HOURS, DAYS), params.timeUnitList);
    }
    @Test
    public void testListOptionParametersAreReusedInstantiatedIfNonNull() {
        EnumParams params = new EnumParams();
        List<TimeUnit> list = new ArrayList<TimeUnit>();
        params.timeUnitList = list;
        new CommandLine(params).parse("-timeUnitList", "DAYS", "HOURS", "DAYS");
        assertEquals(Arrays.asList(DAYS, HOURS, DAYS), params.timeUnitList);
        assertSame(list, params.timeUnitList);
    }
    @Test
    public void testArrayPositionalParametersAreAlwaysInstantiated() {
        class ArrayPositionalParams {
            @picocli.CommandLine.Parameter() int[] array;
        }
        ArrayPositionalParams params = new ArrayPositionalParams();
        params.array = new int[3];
        int[] array = params.array;
        new CommandLine(params).parse("3", "2", "1");
        assertNotSame(array, params.array);
        assertArrayEquals(new int[]{3, 2, 1}, params.array);
    }
    class ListPositionalParams {
        @picocli.CommandLine.Parameter(type = Integer.class) List<Integer> list;
    }
    @Test
    public void testListPositionalParametersAreInstantiatedIfNull() {
        ListPositionalParams params = new ListPositionalParams();
        assertNull(params.list);
        new CommandLine(params).parse("3", "2", "1");
        assertNotNull(params.list);
        assertEquals(Arrays.asList(3, 2, 1), params.list);
    }
    @Test
    public void testListPositionalParametersAreReusedIfNonNull() {
        ListPositionalParams params = new ListPositionalParams();
        params.list = new ArrayList<Integer>();
        List<Integer> list = params.list;
        new CommandLine(params).parse("3", "2", "1");
        assertSame(list, params.list);
        assertEquals(Arrays.asList(3, 2, 1), params.list);
    }

    @Test(expected = DuplicateOptionAnnotationsException.class)
    public void testDuplicateOptionsAreRejected() {
        /** Duplicate parameter names are invalid. */
        class DuplicateOptions {
            @CommandLine.Parameter(names = "-duplicate") public int value1;
            @CommandLine.Parameter(names = "-duplicate") public int value2;
        }
        new CommandLine(new DuplicateOptions());
    }

    private static class FinalFields {
        @CommandLine.Parameter(names = "-f") private final String field = null;
    }
    @Test
    public void testCanInitializeFinalFields() {
        FinalFields ff = CommandLine.parse(new FinalFields(), "-f", "some value");
        assertEquals("some value", ff.field);
    }
    @Test
    public void testLastValueSelectedIfOptionSpecifiedMultipleTimes() {
        FinalFields ff = CommandLine.parse(new FinalFields(), "-f", "111", "-f", "222");
        assertEquals("222", ff.field);
    }

    private static class RequiredField {
        @CommandLine.Parameter(names = {"-h", "--help", "-?", "/?"}, help = true) boolean isHelpRequested;
        @CommandLine.Parameter(names = "--required", required = true) private String required;
    }
    @Test
    public void testErrorIfRequiredOptionNotSpecified() {
        try {
            CommandLine.parse(new RequiredField(), "arg1", "arg2");
            fail("Missing required field should have thrown exception");
        } catch (MissingParameterException ex) {
            assertEquals("Missing required option 'required'", ex.getMessage());
        }
    }
    @Test
    public void testNoErrorIfRequiredOptionSpecified() {
        CommandLine.parse(new RequiredField(), "--required", "arg1", "arg2");
    }
    @Test
    public void testNoErrorIfRequiredOptionNotSpecifiedWhenHelpRequested() {
        RequiredField requiredField = CommandLine.parse(new RequiredField(), "--help");
        assertTrue("help requested", requiredField.isHelpRequested);
    }
    @Test
    public void testHelpRequestedFlagResetWhenParsing_staticMethod() {
        RequiredField requiredField = CommandLine.parse(new RequiredField(), "--help");
        assertTrue("help requested", requiredField.isHelpRequested);

        requiredField.isHelpRequested = false;

        // should throw error again on second pass (no help was requested here...)
        try {
            CommandLine.parse(requiredField, "arg1", "arg2");
            fail("Missing required field should have thrown exception");
        } catch (MissingParameterException ex) {
            assertEquals("Missing required option 'required'", ex.getMessage());
        }
    }
    @Test
    public void testHelpRequestedFlagResetWhenParsing_instanceMethod() {
        RequiredField requiredField = new RequiredField();
        CommandLine commandLine = new CommandLine(requiredField);
        commandLine.parse("--help");
        assertTrue("help requested", requiredField.isHelpRequested);

        requiredField.isHelpRequested = false;

        // should throw error again on second pass (no help was requested here...)
        try {
            commandLine.parse("arg1", "arg2");
            fail("Missing required field should have thrown exception");
        } catch (MissingParameterException ex) {
            assertEquals("Missing required option 'required'", ex.getMessage());
        }
    }

    private class CompactFields {
        @CommandLine.Parameter(names = "-v") boolean verbose;
        @CommandLine.Parameter(names = "-r") boolean recursive;
        @CommandLine.Parameter(names = "-o") File outputFile;
        @picocli.CommandLine.Parameter
        File[] inputFiles;
    }
    @Test
    public void testCompactFieldsAnyOrder() {
        //cmd -a -o arg path path
        //cmd -o arg -a path path
        //cmd -a -o arg -- path path
        //cmd -a -oarg path path
        //cmd -aoarg path path
        CompactFields compact = CommandLine.parse(new CompactFields(), "-rvoout");
        verifyCompact(compact, true, true, "out", null);

        // change order within compact group
        compact = CommandLine.parse(new CompactFields(), "-vroout");
        verifyCompact(compact, true, true, "out", null);

        compact = CommandLine.parse(new CompactFields(), "-rv p1 p2".split(" "));
        verifyCompact(compact, true, true, null, fileArray("p1", "p2"));

        compact = CommandLine.parse(new CompactFields(), "-voout p1 p2".split(" "));
        verifyCompact(compact, true, false, "out", fileArray("p1", "p2"));

        compact = CommandLine.parse(new CompactFields(), "-voout -r p1 p2".split(" "));
        verifyCompact(compact, true, true, "out", fileArray("p1", "p2"));

        compact = CommandLine.parse(new CompactFields(), "-r -v -oout p1 p2".split(" "));
        verifyCompact(compact, true, true, "out", fileArray("p1", "p2"));

        compact = CommandLine.parse(new CompactFields(), "-oout -r -v p1 p2".split(" "));
        verifyCompact(compact, true, true, "out", fileArray("p1", "p2"));

        compact = CommandLine.parse(new CompactFields(), "-rvo out p1 p2".split(" "));
        verifyCompact(compact, true, true, "out", fileArray("p1", "p2"));
    }

    @Test
    public void testCompactWithOptionParamSeparatePlusParameters() {
        CompactFields compact = CommandLine.parse(new CompactFields(), "-r -v -o out p1 p2".split(" "));
        verifyCompact(compact, true, true, "out", fileArray("p1", "p2"));
    }

    @Test
    public void testCompactWithOptionParamAttachedEqualsSeparatorChar() {
        CompactFields compact = CommandLine.parse(new CompactFields(), "-rvo=out p1 p2".split(" "));
        verifyCompact(compact, true, true, "out", fileArray("p1", "p2"));
    }

    @Test
    public void testCompactWithOptionParamAttachedColonSeparatorChar() {
        CompactFields compact = new CompactFields();
        CommandLine cmd = new CommandLine(compact);
        cmd.setSeparator(":");
        cmd.parse("-rvo:out p1 p2".split(" "));
        verifyCompact(compact, true, true, "out", fileArray("p1", "p2"));
    }

    /** @see {@link #testGnuLongOptionsWithVariousSeparators()} */
    @Test
    public void testDefaultSeparatorIsEquals() {
        assertEquals("=", new CommandLine(new CompactFields()).getSeparator());
    }

    @Test
    public void testOptionsAfterParamAreInterpretedAsParameters() {
        CompactFields compact = CommandLine.parse(new CompactFields(), "-r -v p1 -o out p2".split(" "));
        verifyCompact(compact, true, true, null, fileArray("p1", "-o", "out", "p2"));
    }
    @Test
    public void testShortOptionsWithSeparatorButNoValueAssignsEmptyStringEvenIfNotLast() {
        CompactFields compact = CommandLine.parse(new CompactFields(), "-ro= -v".split(" "));
        verifyCompact(compact, true, true, "", null);
    }
    @Test
    public void testShortOptionsWithColonSeparatorButNoValueAssignsEmptyStringEvenIfNotLast() {
        CompactFields compact = new CompactFields();
        CommandLine cmd = new CommandLine(compact);
        cmd.setSeparator(":");
        cmd.parse("-ro: -v".split(" "));
        verifyCompact(compact, true, true, "", null);
    }
    @Test
    public void testShortOptionsWithSeparatorButNoValueAssignsEmptyStringIfLast() {
        CompactFields compact = CommandLine.parse(new CompactFields(), "-rvo=".split(" "));
        verifyCompact(compact, true, true, "", null);
    }


    @Test
    public void testDoubleDashSeparatesPositionalParameters() {
        CompactFields compact = CommandLine.parse(new CompactFields(), "-oout -- -r -v p1 p2".split(" "));
        verifyCompact(compact, false, false, "out", fileArray("-r", "-v", "p1", "p2"));
    }

    private File[] fileArray(final String ... paths) {
        File[] result = new File[paths.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = new File(paths[i]);
        }
        return result;
    }

    private void verifyCompact(CompactFields compact, boolean verbose, boolean recursive, String out, File[] params) {
        assertEquals("-v", verbose, compact.verbose);
        assertEquals("-r", recursive, compact.recursive);
        assertEquals("-o", out == null ? null : new File(out), compact.outputFile);
        if (params == null) {
            assertNull("args", compact.inputFiles);
        } else {
            assertArrayEquals("args", params, compact.inputFiles);
        }
    }

    @Test
    public void testNonSpacedOptions() {
        CompactFields compact = CommandLine.parse(new CompactFields(), "-rvo arg path path".split(" "));
        assertTrue("-r", compact.recursive);
        assertTrue("-v", compact.verbose);
        assertEquals("-o", new File("arg"), compact.outputFile);
        assertArrayEquals("args", new File[]{new File("path"), new File("path")}, compact.inputFiles);
    }

    @Test
    public void testPrimitiveParameters() {
        class PrimitiveIntParameters {
            @picocli.CommandLine.Parameter
            int[] intParams;
        }
        PrimitiveIntParameters params = CommandLine.parse(new PrimitiveIntParameters(), "1 2 3 4".split(" "));
        assertArrayEquals(new int[] {1, 2, 3, 4}, params.intParams);
    }

    @Test
    public void testArityConstructor_fixedRange() {
        Arity arity = new Arity(1, 23, false, false, null);
        assertEquals("min", 1, arity.min);
        assertEquals("max", 23, arity.max);
        assertEquals("1..23", arity.toString());
        assertEquals(Arity.valueOf("1..23"), arity);
    }
    @Test
    public void testArityConstructor_variableRange() {
        Arity arity = new Arity(1, Integer.MAX_VALUE, true, false, null);
        assertEquals("min", 1, arity.min);
        assertEquals("max", Integer.MAX_VALUE, arity.max);
        assertEquals("1..*", arity.toString());
        assertEquals(Arity.valueOf("1..*"), arity);
    }
    @Test
    public void testArityForOption_booleanFieldImplicitArity0() throws Exception {
        Arity arity = Arity.forOption(SupportedTypes.class.getDeclaredField("booleanField"));
        assertEquals(Arity.valueOf("0"), arity);
        assertEquals("0", arity.toString());
    }
    @Test
    public void testArityForOption_intFieldImplicitArity1() throws Exception {
        Arity arity = Arity.forOption(SupportedTypes.class.getDeclaredField("intField"));
        assertEquals(Arity.valueOf("1"), arity);
        assertEquals("1", arity.toString());
    }
    @Test
    public void testArityForOption_isExplicitlyDeclaredValue() throws Exception {
        Arity arity = Arity.forOption(EnumParams.class.getDeclaredField("timeUnitList"));
        assertEquals(Arity.valueOf("3"), arity);
        assertEquals("3", arity.toString());
    }
    @Test
    public void testArityForOption_listFieldImplicitArity0_n() throws Exception {
        class ImplicitList { @CommandLine.Parameter(names = "-a") List<Integer> listIntegers; }
        Arity arity = Arity.forOption(ImplicitList.class.getDeclaredField("listIntegers"));
        assertEquals(Arity.valueOf("0..*"), arity);
        assertEquals("0..*", arity.toString());
    }
    @Test
    public void testArityForOption_arrayFieldImplicitArity0_n() throws Exception {
        class ImplicitList { @CommandLine.Parameter(names = "-a") int[] intArray; }
        Arity arity = Arity.forOption(ImplicitList.class.getDeclaredField("intArray"));
        assertEquals(Arity.valueOf("0..*"), arity);
        assertEquals("0..*", arity.toString());
    }
    @Test
    public void testArityForParameters_booleanFieldImplicitArity0() throws Exception {
        class ImplicitBoolField { @picocli.CommandLine.Parameter
        boolean boolSingleValue; }
        Arity arity = Arity.forParameters(ImplicitBoolField.class.getDeclaredField("boolSingleValue"));
        assertEquals(Arity.valueOf("0"), arity);
        assertEquals("0", arity.toString());
    }
    @Test
    public void testArityForParameters_intFieldImplicitArity1() throws Exception {
        class ImplicitSingleField { @picocli.CommandLine.Parameter
        int intSingleValue; }
        Arity arity = Arity.forParameters(ImplicitSingleField.class.getDeclaredField("intSingleValue"));
        assertEquals(Arity.valueOf("1"), arity);
        assertEquals("1", arity.toString());
    }
    @Test
    public void testArityForParameters_listFieldImplicitArity0_n() throws Exception {
        Arity arity = Arity.forParameters(ListPositionalParams.class.getDeclaredField("list"));
        assertEquals(Arity.valueOf("0..*"), arity);
        assertEquals("0..*", arity.toString());
    }
    @Test
    public void testArityForParameters_arrayFieldImplicitArity0_n() throws Exception {
        Arity arity = Arity.forParameters(CompactFields.class.getDeclaredField("inputFiles"));
        assertEquals(Arity.valueOf("0..*"), arity);
        assertEquals("0..*", arity.toString());
    }
    @Test
    public void testArrayOptionsWithArity0_nConsumeAllArguments() {
        final double[] DEFAULT_PARAMS = new double[] {1, 2};
        class ArrayOptionsArity0_nAndParameters {
            @picocli.CommandLine.Parameter
            double[] doubleParams = DEFAULT_PARAMS;
            @CommandLine.Parameter(names = "-doubles", arity = "0..*") double[] doubleOptions;
        }
        ArrayOptionsArity0_nAndParameters
                params = CommandLine.parse(new ArrayOptionsArity0_nAndParameters(), "-doubles 1.1 2.2 3.3 4.4".split(" "));
        assertArrayEquals(Arrays.toString(params.doubleOptions),
                new double[] {1.1, 2.2, 3.3, 4.4}, params.doubleOptions, 0.000001);
        assertArrayEquals(DEFAULT_PARAMS, params.doubleParams, 0.000001);
    }

    @Test
    public void testArrayOptionsWithArity1_nConsumeAllArguments() {
        class ArrayOptionsArity1_nAndParameters {
            @picocli.CommandLine.Parameter
            double[] doubleParams;
            @CommandLine.Parameter(names = "-doubles", arity = "1..*") double[] doubleOptions;
        }
        ArrayOptionsArity1_nAndParameters
                params = CommandLine.parse(new ArrayOptionsArity1_nAndParameters(), "-doubles 1.1 2.2 3.3 4.4".split(" "));
        assertArrayEquals(Arrays.toString(params.doubleOptions),
                new double[] {1.1, 2.2, 3.3, 4.4}, params.doubleOptions, 0.000001);
        assertArrayEquals(null, params.doubleParams, 0.000001);
    }

    @Test
    public void testArrayOptionsWithArity2_nConsumeAllArguments() {
        class ArrayOptionsArity2_nAndParameters {
            @picocli.CommandLine.Parameter
            double[] doubleParams;
            @CommandLine.Parameter(names = "-doubles", arity = "2..*") double[] doubleOptions;
        }
        ArrayOptionsArity2_nAndParameters
                params = CommandLine.parse(new ArrayOptionsArity2_nAndParameters(), "-doubles 1.1 2.2 3.3 4.4".split(" "));
        assertArrayEquals(Arrays.toString(params.doubleOptions),
                new double[] {1.1, 2.2, 3.3, 4.4}, params.doubleOptions, 0.000001);
        assertArrayEquals(null, params.doubleParams, 0.000001);
    }

    @Test
    public void testArrayOptionArity2_nConsumesAllArgumentsUpToClusteredOption() {
        class ArrayOptionsArity2_nAndParameters {
            @picocli.CommandLine.Parameter
            String[] stringParams;
            @CommandLine.Parameter(names = "-s", arity = "2..*") String[] stringOptions;
            @CommandLine.Parameter(names = "-v") boolean verbose;
            @CommandLine.Parameter(names = "-f") File file;
        }
        ArrayOptionsArity2_nAndParameters
                params = CommandLine.parse(new ArrayOptionsArity2_nAndParameters(), "-s 1.1 2.2 3.3 4.4 -vfFILE 5.5".split(" "));
        assertArrayEquals(Arrays.toString(params.stringOptions),
                new String[] {"1.1", "2.2", "3.3", "4.4"}, params.stringOptions);
        assertTrue(params.verbose);
        assertEquals(new File("FILE"), params.file);
        assertArrayEquals(new String[] {"5.5"}, params.stringParams);
    }

    @Test
    public void testArrayOptionArity2_nConsumesAllArgumentIncludingQuotedSimpleOption() {
        class ArrayOptionArity2_nAndParameters {
            @picocli.CommandLine.Parameter
            String[] stringParams;
            @CommandLine.Parameter(names = "-s", arity = "2..*") String[] stringOptions;
            @CommandLine.Parameter(names = "-v") boolean verbose;
            @CommandLine.Parameter(names = "-f") File file;
        }
        ArrayOptionArity2_nAndParameters
                params = CommandLine.parse(new ArrayOptionArity2_nAndParameters(), "-s 1.1 2.2 3.3 4.4 \"-v\" \"-f\" \"FILE\" 5.5".split(" "));
        assertArrayEquals(Arrays.toString(params.stringOptions),
                new String[] {"1.1", "2.2", "3.3", "4.4", "-v", "-f", "FILE", "5.5"}, params.stringOptions);
        assertFalse("verbose", params.verbose);
        assertNull("file", params.file);
        assertArrayEquals(null, params.stringParams);
    }

    @Test
    public void testArrayOptionArity2_nConsumesAllArgumentIncludingQuotedClusteredOption() {
        class ArrayOptionArity2_nAndParameters {
            @picocli.CommandLine.Parameter
            String[] stringParams;
            @CommandLine.Parameter(names = "-s", arity = "2..*") String[] stringOptions;
            @CommandLine.Parameter(names = "-v") boolean verbose;
            @CommandLine.Parameter(names = "-f") File file;
        }
        ArrayOptionArity2_nAndParameters
                params = CommandLine.parse(new ArrayOptionArity2_nAndParameters(), "-s 1.1 2.2 3.3 4.4 \"-vfFILE\" 5.5".split(" "));
        assertArrayEquals(Arrays.toString(params.stringOptions),
                new String[] {"1.1", "2.2", "3.3", "4.4", "-vfFILE", "5.5"}, params.stringOptions);
        assertFalse("verbose", params.verbose);
        assertNull("file", params.file);
        assertArrayEquals(null, params.stringParams);
    }

    @Test
    public void testArrayOptionArity2_nConsumesAllArgumentsUpToNextSimpleOption() {
        class ArrayOptionArity2_nAndParameters {
            @picocli.CommandLine.Parameter
            double[] doubleParams;
            @CommandLine.Parameter(names = "-s", arity = "2..*") String[] stringOptions;
            @CommandLine.Parameter(names = "-v") boolean verbose;
            @CommandLine.Parameter(names = "-f") File file;
        }
        ArrayOptionArity2_nAndParameters
                params = CommandLine.parse(new ArrayOptionArity2_nAndParameters(), "-s 1.1 2.2 3.3 4.4 -v -f=FILE 5.5".split(" "));
        assertArrayEquals(Arrays.toString(params.stringOptions),
                new String[] {"1.1", "2.2", "3.3", "4.4"}, params.stringOptions);
        assertTrue(params.verbose);
        assertEquals(new File("FILE"), params.file);
        assertArrayEquals(new double[] {5.5}, params.doubleParams, 0.000001);
    }

    @Test
    public void testArrayOptionArity2_nConsumesAllArgumentsUpToNextOptionWithAttachment() {
        class ArrayOptionArity2_nAndParameters {
            @picocli.CommandLine.Parameter
            double[] doubleParams;
            @CommandLine.Parameter(names = "-s", arity = "2..*") String[] stringOptions;
            @CommandLine.Parameter(names = "-v") boolean verbose;
            @CommandLine.Parameter(names = "-f") File file;
        }
        ArrayOptionArity2_nAndParameters
                params = CommandLine.parse(new ArrayOptionArity2_nAndParameters(), "-s 1.1 2.2 3.3 4.4 -f=FILE -v 5.5".split(" "));
        assertArrayEquals(Arrays.toString(params.stringOptions),
                new String[] {"1.1", "2.2", "3.3", "4.4"}, params.stringOptions);
        assertTrue(params.verbose);
        assertEquals(new File("FILE"), params.file);
        assertArrayEquals(new double[] {5.5}, params.doubleParams, 0.000001);
    }

    @Test
    public void testArrayOptionArityNConsumeAllArguments() {
        class ArrayOptionArityNAndParameters {
            @picocli.CommandLine.Parameter
            char[] charParams;
            @CommandLine.Parameter(names = "-chars", arity = "*") char[] charOptions;
        }
        ArrayOptionArityNAndParameters
                params = CommandLine.parse(new ArrayOptionArityNAndParameters(), "-chars a b c d".split(" "));
        assertArrayEquals(Arrays.toString(params.charOptions),
                new char[] {'a', 'b', 'c', 'd'}, params.charOptions);
        assertArrayEquals(null, params.charParams);
    }

    private static class BooleanOptionsArity0_nAndParameters {
        @picocli.CommandLine.Parameter
        String[] params;
        @CommandLine.Parameter(names = "-bool", arity = "0..*") boolean bool;
        @CommandLine.Parameter(names = {"-v", "-other"}, arity="0..*") boolean vOrOther;
        @CommandLine.Parameter(names = "-r") boolean rBoolean;
    }
    @Test
    public void testBooleanOptionsArity0_nConsume1ArgumentIfPossible() { // ignores varargs
        BooleanOptionsArity0_nAndParameters
                params = CommandLine.parse(new BooleanOptionsArity0_nAndParameters(), "-bool false false true".split(" "));
        assertFalse(params.bool);
        assertArrayEquals(new String[]{ "false", "true"}, params.params);
    }
    @Test
    public void testBooleanOptionsArity0_nRequiresNoArgument() { // ignores varargs
        BooleanOptionsArity0_nAndParameters
                params = CommandLine.parse(new BooleanOptionsArity0_nAndParameters(), "-bool".split(" "));
        assertTrue(params.bool);
    }
    @Test
    public void testBooleanOptionsArity0_nConsume0ArgumentsIfNextArgIsOption() { // ignores varargs
        BooleanOptionsArity0_nAndParameters
                params = CommandLine.parse(new BooleanOptionsArity0_nAndParameters(), "-bool -other".split(" "));
        assertTrue(params.bool);
        assertTrue(params.vOrOther);
    }
    @Test
    public void testBooleanOptionsArity0_nConsume0ArgumentsIfNextArgIsParameter() { // ignores varargs
        BooleanOptionsArity0_nAndParameters
                params = CommandLine.parse(new BooleanOptionsArity0_nAndParameters(), "-bool 123 -other".split(" "));
        assertTrue(params.bool);
        assertFalse(params.vOrOther);
        assertArrayEquals(new String[]{ "123", "-other"}, params.params);
    }
    @Test
    public void testBooleanOptionsArity0_nFailsIfAttachedParamNotABoolean() { // ignores varargs
        try {
            CommandLine.parse(new BooleanOptionsArity0_nAndParameters(), "-bool=123 -other".split(" "));
            fail("was able to assign 123 to boolean");
        } catch (ParameterException ex) {
            assertEquals("'123' is not a boolean for option '-bool'", ex.getMessage());
        }
    }
    @Test
    public void testBooleanOptionsArity0_nShortFormFailsIfAttachedParamNotABoolean() { // ignores varargs
        BooleanOptionsArity0_nAndParameters params =
            CommandLine.parse(new BooleanOptionsArity0_nAndParameters(), "-rv234 -bool".split(" "));
        assertTrue(params.vOrOther);
        assertTrue(params.rBoolean);
        assertArrayEquals(new String[]{ "234", "-bool"}, params.params);
        assertFalse(params.bool);
    }
    @Test
    public void testBooleanOptionsArity0_nShortFormFailsIfAttachedWithSepParamNotABoolean() { // ignores varargs
        try {
            CommandLine.parse(new BooleanOptionsArity0_nAndParameters(), "-rv=234 -bool".split(" "));
            fail("was able to assign 234 to boolean");
        } catch (ParameterException ex) {
            assertEquals("'234' is not a boolean for option '-v'", ex.getMessage());
        }
    }

    private static class BooleanOptionsArity1_nAndParameters {
        @picocli.CommandLine.Parameter
        boolean[] boolParams;
        @CommandLine.Parameter(names = "-bool", arity = "1..*") boolean aBoolean;
    }
    @Test
    public void testBooleanOptionsArity1_nConsume1Argument() { // ignores varargs
        BooleanOptionsArity1_nAndParameters
                params = CommandLine.parse(new BooleanOptionsArity1_nAndParameters(), "-bool false false true".split(" "));
        assertFalse(params.aBoolean);
        assertArrayEquals(new boolean[]{ false, true}, params.boolParams);

        params = CommandLine.parse(new BooleanOptionsArity1_nAndParameters(), "-bool true false true".split(" "));
        assertTrue(params.aBoolean);
        assertArrayEquals(new boolean[]{ false, true}, params.boolParams);
    }
    @Test
    public void testBooleanOptionsArity1_nCaseInsensitive() { // ignores varargs
        BooleanOptionsArity1_nAndParameters
                params = CommandLine.parse(new BooleanOptionsArity1_nAndParameters(), "-bool fAlsE false true".split(" "));
        assertFalse(params.aBoolean);
        assertArrayEquals(new boolean[]{ false, true}, params.boolParams);

        params = CommandLine.parse(new BooleanOptionsArity1_nAndParameters(), "-bool FaLsE false true".split(" "));
        assertFalse(params.aBoolean);
        assertArrayEquals(new boolean[]{ false, true}, params.boolParams);

        params = CommandLine.parse(new BooleanOptionsArity1_nAndParameters(), "-bool tRuE false true".split(" "));
        assertTrue(params.aBoolean);
        assertArrayEquals(new boolean[]{ false, true}, params.boolParams);
    }
    @Test
    public void testBooleanOptionsArity1_nErrorIfValueNotTrueOrFalse() { // ignores varargs
        try {
            CommandLine.parse(new BooleanOptionsArity1_nAndParameters(), "-bool abc".split(" "));
            fail("Invalid format abc was accepted for boolean");
        } catch (ParameterException expected) {
            assertEquals("'abc' is not a boolean for option '-bool'", expected.getMessage());
        }
    }

    @Test
    public void testBooleanOptionArity0Consumes0Arguments() {
        class BooleanOptionArity0AndParameters {
            @picocli.CommandLine.Parameter
            boolean[] boolParams;
            @CommandLine.Parameter(names = "-bool", arity = "0") boolean aBoolean;
        }
        BooleanOptionArity0AndParameters
                params = CommandLine.parse(new BooleanOptionArity0AndParameters(), "-bool true false true".split(" "));
        assertTrue(params.aBoolean);
        assertArrayEquals(new boolean[]{true, false, true}, params.boolParams);
    }

    @Test
    public void testIntOptionArity1_nConsumes1Argument() { // ignores varargs
        class IntOptionArity1_nAndParameters {
            @picocli.CommandLine.Parameter
            int[] intParams;
            @CommandLine.Parameter(names = "-int", arity = "1..*") int anInt;
        }
        IntOptionArity1_nAndParameters
                params = CommandLine.parse(new IntOptionArity1_nAndParameters(), "-int 23 42 7".split(" "));
        assertEquals(23, params.anInt);
        assertArrayEquals(new int[]{ 42, 7}, params.intParams);
    }

    @Test
    public void testArrayOptionsWithArity0Consume0Arguments() {
        class OptionsArray0ArityAndParameters {
            @picocli.CommandLine.Parameter
            double[] doubleParams;
            @CommandLine.Parameter(names = "-doubles", arity = "0") double[] doubleOptions;
        }
        OptionsArray0ArityAndParameters
                params = CommandLine.parse(new OptionsArray0ArityAndParameters(), "-doubles 1.1 2.2 3.3 4.4".split(" "));
        assertArrayEquals(Arrays.toString(params.doubleOptions),
                new double[0], params.doubleOptions, 0.000001);
        assertArrayEquals(new double[]{1.1, 2.2, 3.3, 4.4}, params.doubleParams, 0.000001);
    }

    @Test
    public void testArrayOptionWithArity1Consumes1Argument() {
        class Options1ArityAndParameters {
            @picocli.CommandLine.Parameter
            double[] doubleParams;
            @CommandLine.Parameter(names = "-doubles", arity = "1") double[] doubleOptions;
        }
        Options1ArityAndParameters
                params = CommandLine.parse(new Options1ArityAndParameters(), "-doubles 1.1 2.2 3.3 4.4".split(" "));
        assertArrayEquals(Arrays.toString(params.doubleOptions),
                new double[] {1.1}, params.doubleOptions, 0.000001);
        assertArrayEquals(new double[]{2.2, 3.3, 4.4}, params.doubleParams, 0.000001);
    }

    private static class ArrayOptionArity2AndParameters {
        @picocli.CommandLine.Parameter
        double[] doubleParams;
        @CommandLine.Parameter(names = "-doubles", arity = "2") double[] doubleOptions;
    }
    @Test
    public void testArrayOptionWithArity2Consumes2Arguments() {
        ArrayOptionArity2AndParameters
                params = CommandLine.parse(new ArrayOptionArity2AndParameters(), "-doubles 1.1 2.2 3.3 4.4".split(" "));
        assertArrayEquals(Arrays.toString(params.doubleOptions),
                new double[] {1.1, 2.2, }, params.doubleOptions, 0.000001);
        assertArrayEquals(new double[]{3.3, 4.4}, params.doubleParams, 0.000001);
    }
    @Test
    public void testArrayOptionsWithArity2Consume2ArgumentsEvenIfFirstIsAttached() {
        ArrayOptionArity2AndParameters
                params = CommandLine.parse(new ArrayOptionArity2AndParameters(), "-doubles=1.1 2.2 3.3 4.4".split(" "));
        assertArrayEquals(Arrays.toString(params.doubleOptions),
                new double[] {1.1, 2.2, }, params.doubleOptions, 0.000001);
        assertArrayEquals(new double[]{3.3, 4.4}, params.doubleParams, 0.000001);
    }

    @Test
    public void testArrayOptionWithoutArityConsumesAllArguments() {
        class OptionsNoArityAndParameters {
            @picocli.CommandLine.Parameter
            char[] charParams;
            @CommandLine.Parameter(names = "-chars") char[] charOptions;
        }
        OptionsNoArityAndParameters
                params = CommandLine.parse(new OptionsNoArityAndParameters(), "-chars a b c d".split(" "));
        assertArrayEquals(Arrays.toString(params.charOptions),
                new char[] {'a', 'b', 'c', 'd'}, params.charOptions);
        assertArrayEquals(Arrays.toString(params.charParams), null, params.charParams);
    }

    @Test(expected = MissingTypeConverterException.class)
    public void testMissingTypeConverter() {
        class MissingConverter {
            @CommandLine.Parameter(names = "--socket") Socket socket;
        }
        CommandLine.parse(new MissingConverter(), "--socket anyString".split(" "));
    }

    @Test
    public void testArrayParametersWithArityMinusOneToN() {
        class ArrayParamsNegativeArity {
            @picocli.CommandLine.Parameter(arity = "-1..*")
            List<String> params;
        }
        ArrayParamsNegativeArity params = CommandLine.parse(new ArrayParamsNegativeArity(), "a", "b", "c");
        assertEquals(Arrays.asList("a", "b", "c"), params.params);

        params = CommandLine.parse(new ArrayParamsNegativeArity(), "a");
        assertEquals(Arrays.asList("a"), params.params);

        params = CommandLine.parse(new ArrayParamsNegativeArity());
        assertEquals(null, params.params);
    }

    @Test
    public void testArrayParametersArity0_n() {
        class ArrayParamsArity0_n {
            @picocli.CommandLine.Parameter(arity = "0..*")
            List<String> params;
        }
        ArrayParamsArity0_n params = CommandLine.parse(new ArrayParamsArity0_n(), "a", "b", "c");
        assertEquals(Arrays.asList("a", "b", "c"), params.params);

        params = CommandLine.parse(new ArrayParamsArity0_n(), "a");
        assertEquals(Arrays.asList("a"), params.params);

        params = CommandLine.parse(new ArrayParamsArity0_n());
        assertEquals(null, params.params);
    }

    @Test
    public void testArrayParametersArity1_n() {
        class ArrayParamsArity1_n {
            @picocli.CommandLine.Parameter(arity = "1..*")
            List<String> params;
        }
        ArrayParamsArity1_n params = CommandLine.parse(new ArrayParamsArity1_n(), "a", "b", "c");
        assertEquals(Arrays.asList("a", "b", "c"), params.params);

        params = CommandLine.parse(new ArrayParamsArity1_n(), "a");
        assertEquals(Arrays.asList("a"), params.params);

        try {
            params = CommandLine.parse(new ArrayParamsArity1_n());
            fail("Should not accept input with missing parameter");
        } catch (MissingParameterException ex) {
            assertEquals("Missing required parameter for field 'params'", ex.getMessage());
        }
    }

    @Test
    public void testArrayParametersArity2_n() {
        class ArrayParamsArity2_n {
            @picocli.CommandLine.Parameter(arity = "2..*")
            List<String> params;
        }
        ArrayParamsArity2_n params = CommandLine.parse(new ArrayParamsArity2_n(), "a", "b", "c");
        assertEquals(Arrays.asList("a", "b", "c"), params.params);

        try {
            params = CommandLine.parse(new ArrayParamsArity2_n(), "a");
            fail("Should not accept input with missing parameter");
        } catch (MissingParameterException ex) {
            assertEquals("Field 'params' requires at least 2 parameters, but only 1 were specified.", ex.getMessage());
        }

        try {
            params = CommandLine.parse(new ArrayParamsArity2_n());
            fail("Should not accept input with missing parameter");
        } catch (MissingParameterException ex) {
            assertEquals("Field 'params' requires at least 2 parameters, but only 0 were specified.", ex.getMessage());
        }
    }

    @Test
    public void testNonVarargArrayParametersWithNegativeArityConsumesZeroArguments() {
        class NonVarArgArrayParamsNegativeArity {
            @picocli.CommandLine.Parameter(arity = "-1")
            List<String> params;
        }
        NonVarArgArrayParamsNegativeArity params = CommandLine.parse(new NonVarArgArrayParamsNegativeArity(), "a", "b", "c");
        assertEquals(Arrays.asList(), params.params);

        params = CommandLine.parse(new NonVarArgArrayParamsNegativeArity(), "a");
        assertEquals(Arrays.asList(), params.params);

        params = CommandLine.parse(new NonVarArgArrayParamsNegativeArity());
        assertEquals(null, params.params);
    }

    @Test
    public void testNonVarargArrayParametersWithArity0() {
        class NonVarArgArrayParamsZeroArity {
            @picocli.CommandLine.Parameter(arity = "0")
            List<String> params;
        }
        NonVarArgArrayParamsZeroArity params = CommandLine.parse(new NonVarArgArrayParamsZeroArity(), "a", "b", "c");
        assertEquals(new ArrayList<String>(), params.params);

        params = CommandLine.parse(new NonVarArgArrayParamsZeroArity(), "a");
        assertEquals(new ArrayList<String>(), params.params);

        params = CommandLine.parse(new NonVarArgArrayParamsZeroArity());
        assertEquals(null, params.params);
    }

    @Test
    public void testNonVarargArrayParametersWithArity1() {
        class NonVarArgArrayParamsArity1 {
            @picocli.CommandLine.Parameter(arity = "1")
            List<String> params;
        }
        NonVarArgArrayParamsArity1 params = CommandLine.parse(new NonVarArgArrayParamsArity1(), "a", "b", "c");
        assertEquals(Arrays.asList("a"), params.params);

        params = CommandLine.parse(new NonVarArgArrayParamsArity1(), "a");
        assertEquals(Arrays.asList("a"), params.params);

        try {
            params = CommandLine.parse(new NonVarArgArrayParamsArity1());
            fail("Should not accept input with missing parameter");
        } catch (MissingParameterException ex) {
            assertEquals("Missing required parameter for field 'params'", ex.getMessage());
        }
    }

    @Test
    public void testNonVarargArrayParametersWithArity2() {
        class NonVarArgArrayParamsArity2 {
            @picocli.CommandLine.Parameter(arity = "2")
            List<String> params;
        }
        NonVarArgArrayParamsArity2 params = CommandLine.parse(new NonVarArgArrayParamsArity2(), "a", "b", "c");
        assertEquals(Arrays.asList("a", "b"), params.params);

        try {
            params = CommandLine.parse(new NonVarArgArrayParamsArity2(), "a");
            fail("Should not accept input with missing parameter");
        } catch (MissingParameterException ex) {
            assertEquals("Field 'params' requires at least 2 parameters, but only 1 were specified.", ex.getMessage());
        }

        try {
            params = CommandLine.parse(new NonVarArgArrayParamsArity2());
            fail("Should not accept input with missing parameter");
        } catch (MissingParameterException ex) {
            assertEquals("Field 'params' requires at least 2 parameters, but only 0 were specified.", ex.getMessage());
        }
    }

    class VariousPrefixCharacters {
        @CommandLine.Parameter(names = {"-d", "--dash"}) int dash;
        @CommandLine.Parameter(names = {"/S"}) int slashS;
        @CommandLine.Parameter(names = {"/T"}) int slashT;
        @CommandLine.Parameter(names = {"/4"}) boolean fourDigit;
        @CommandLine.Parameter(names = {"/Owner", "--owner"}) String owner;
        @CommandLine.Parameter(names = {"-SingleDash"}) boolean singleDash;
        @CommandLine.Parameter(names = {"[CPM"}) String cpm;
        @CommandLine.Parameter(names = {"(CMS"}) String cms;
    }
    @Test
    public void testOptionsMayDefineAnyPrefixChar() {
        VariousPrefixCharacters params = CommandLine.parse(new VariousPrefixCharacters(),
                "-d 123 /4 /S 765 /T=98 /Owner=xyz -SingleDash [CPM CP/M (CMS=cmsVal".split(" "));
        assertEquals("-d", 123, params.dash);
        assertEquals("/S", 765, params.slashS);
        assertEquals("/T", 98, params.slashT);
        assertTrue("/4", params.fourDigit);
        assertTrue("-SingleDash", params.singleDash);
        assertEquals("/Owner", "xyz", params.owner);
        assertEquals("[CPM", "CP/M", params.cpm);
        assertEquals("(CMS", "cmsVal", params.cms);
    }
    @Test
    public void testGnuLongOptionsWithVariousSeparators() {
        VariousPrefixCharacters params = CommandLine.parse(new VariousPrefixCharacters(), "--dash 123".split(" "));
        assertEquals("--dash val", 123, params.dash);

        params = CommandLine.parse(new VariousPrefixCharacters(), "--dash=234 --owner=x".split(" "));
        assertEquals("--dash=val", 234, params.dash);
        assertEquals("--owner=x", "x", params.owner);

        params = new VariousPrefixCharacters();
        CommandLine cmd = new CommandLine(params);
        cmd.setSeparator(":");
        cmd.parse("--dash:345");
        assertEquals("--dash:val", 345, params.dash);

        params = new VariousPrefixCharacters();
        cmd = new CommandLine(params);
        cmd.setSeparator(":");
        cmd.parse("--dash:345 --owner:y".split(" "));
        assertEquals("--dash:val", 345, params.dash);
        assertEquals("--owner:y", "y", params.owner);
    }
    @Test
    public void testGnuLongOptionsWithVariousSeparatorsOnlyAndNoValue() {
        VariousPrefixCharacters params;
        try {
            params = CommandLine.parse(new VariousPrefixCharacters(), "--dash".split(" "));
            fail("int option needs arg");
        } catch (ParameterException ex) {
            assertEquals("Missing required parameter for field 'dash'", ex.getMessage());
        }

        try {
            params = CommandLine.parse(new VariousPrefixCharacters(), "--owner".split(" "));
        } catch (ParameterException ex) {
            assertEquals("Missing required parameter for field 'owner'", ex.getMessage());
        }

        params = CommandLine.parse(new VariousPrefixCharacters(), "--owner=".split(" "));
        assertEquals("--owner= (at end)", "", params.owner);

        params = CommandLine.parse(new VariousPrefixCharacters(), "--owner= /4".split(" "));
        assertEquals("--owner= (in middle)", "", params.owner);
        assertEquals("/4", true, params.fourDigit);

        try {
            params = CommandLine.parse(new VariousPrefixCharacters(), "--dash=".split(" "));
            fail("int option (with sep but no value) needs arg");
        } catch (ParameterException ex) {
            assertEquals("Could not convert '' to int for option '-d'", ex.getMessage());
        }

        try {
            params = CommandLine.parse(new VariousPrefixCharacters(), "--dash= /4".split(" "));
            fail("int option (with sep but no value, followed by other option) needs arg");
        } catch (ParameterException ex) {
            assertEquals("Could not convert '' to int for option '-d'", ex.getMessage());
        }
    }

    @Test
    public void testOptionParameterSeparatorIsCustomizable() {
        VariousPrefixCharacters params = new VariousPrefixCharacters();
        CommandLine cmd = new CommandLine(params);
        cmd.setSeparator(":");
        cmd.parse("-d 123 /4 /S 765 /T:98 /Owner:xyz -SingleDash [CPM CP/M (CMS:cmsVal".split(" "));
        assertEquals("-d", 123, params.dash);
        assertEquals("/S", 765, params.slashS);
        assertEquals("/T", 98, params.slashT);
        assertTrue("/4", params.fourDigit);
        assertTrue("-SingleDash", params.singleDash);
        assertEquals("/Owner", "xyz", params.owner);
        assertEquals("[CPM", "CP/M", params.cpm);
        assertEquals("(CMS", "cmsVal", params.cms);
    }
    @Test(expected = NullPointerException.class)
    public void testOptionParameterSeparatorCannotBeSetToNull() {
        CommandLine cmd = new CommandLine(new VariousPrefixCharacters());
        cmd.setSeparator(null);
    }

    @Test
    public void testPotentiallyNestedOptionParsedCorrectly() {
        class MyOption {
            @CommandLine.Parameter(names = "-p") String path;
        }
        MyOption opt = CommandLine.parse(new MyOption(), "-pa-p");
        assertEquals("a-p", opt.path);

        opt = CommandLine.parse(new MyOption(), "-p-ap");
        assertEquals("-ap", opt.path);
    }

    @Test
    public void testArityGreaterThanOneForSingleValuedFields() {
        class Arity2 {
            @CommandLine.Parameter(names = "-p", arity="2") String path;
            @CommandLine.Parameter(names = "-o", arity="2") String[] otherPath;
        }
        Arity2 opt = CommandLine.parse(new Arity2(), "-o a b".split(" "));

        opt = CommandLine.parse(new Arity2(), "-p a b".split(" "));
        assertEquals("a", opt.path);
    }

    @Test
    public void testOptionParameterQuotesRemovedFromValue() {
        class TextOption {
            @CommandLine.Parameter(names = "-t") String text;
        }
        TextOption opt = CommandLine.parse(new TextOption(), "-t", "\"a text\"");
        assertEquals("a text", opt.text);
    }

    @Test
    public void testLongOptionAttachedQuotedParameterQuotesRemovedFromValue() {
        class TextOption {
            @CommandLine.Parameter(names = "--text") String text;
        }
        TextOption opt = CommandLine.parse(new TextOption(), "--text=\"a text\"");
        assertEquals("a text", opt.text);
    }

    @Test
    public void testShortOptionAttachedQuotedParameterQuotesRemovedFromValue() {
        class TextOption {
            @CommandLine.Parameter(names = "-t") String text;
        }
        TextOption opt = CommandLine.parse(new TextOption(), "-t\"a text\"");
        assertEquals("a text", opt.text);

        opt = CommandLine.parse(new TextOption(), "-t=\"a text\"");
        assertEquals("a text", opt.text);
    }

    @Test
    public void testShortOptionQuotedParameterTypeConversion() {
        class TextOption {
            @CommandLine.Parameter(names = "-t") int[] number;
            @CommandLine.Parameter(names = "-v", arity = "1") boolean verbose;
        }
        TextOption opt = CommandLine.parse(new TextOption(), "-t", "\"123\"", "-v", "\"true\"");
        assertEquals(123, opt.number[0]);
        assertTrue(opt.verbose);

        opt = CommandLine.parse(new TextOption(), "-t\"123\"", "-v\"true\"");
        assertEquals(123, opt.number[0]);
        assertTrue(opt.verbose);

        opt = CommandLine.parse(new TextOption(), "-t=\"345\"", "-v=\"true\"");
        assertEquals(345, opt.number[0]);
        assertTrue(opt.verbose);
    }

    @Test
    public void testOptionMultiParameterQuotesRemovedFromValue() {
        class TextOption {
            @CommandLine.Parameter(names = "-t") String[] text;
        }
        TextOption opt = CommandLine.parse(new TextOption(), "-t", "\"a text\"", "\"another text\"", "\"x z\"");
        assertArrayEquals(new String[]{"a text", "another text", "x z"}, opt.text);

        opt = CommandLine.parse(new TextOption(), "-t\"a text\"", "\"another text\"", "\"x z\"");
        assertArrayEquals(new String[]{"a text", "another text", "x z"}, opt.text);

        opt = CommandLine.parse(new TextOption(), "-t=\"a text\"", "\"another text\"", "\"x z\"");
        assertArrayEquals(new String[]{"a text", "another text", "x z"}, opt.text);
    }

    @Test
    public void testPositionalParameterQuotesRemovedFromValue() {
        class TextParams {
            @picocli.CommandLine.Parameter() String[] text;
        }
        TextParams opt = CommandLine.parse(new TextParams(), "\"a text\"");
        assertEquals("a text", opt.text[0]);
    }

    @Test
    public void testPositionalMultiParameterQuotesRemovedFromValue() {
        class TextParams {
            @picocli.CommandLine.Parameter() String[] text;
        }
        TextParams opt = CommandLine.parse(new TextParams(), "\"a text\"", "\"another text\"", "\"x z\"");
        assertArrayEquals(new String[]{"a text", "another text", "x z"}, opt.text);
    }

    @Test
    public void testPositionalMultiQuotedParameterTypeConversion() {
        class TextParams {
            @picocli.CommandLine.Parameter() int[] numbers;
        }
        TextParams opt = CommandLine.parse(new TextParams(), "\"123\"", "\"456\"", "\"999\"");
        assertArrayEquals(new int[]{123, 456, 999}, opt.numbers);
    }

    @Test
    public void testSubclassedOptions() {
        class ParentOption {
            @CommandLine.Parameter(names = "-p") String path;
        }
        class ChildOption extends ParentOption {
            @CommandLine.Parameter(names = "-t") String text;
        }
        ChildOption opt = CommandLine.parse(new ChildOption(), "-p", "somePath", "-t", "\"a text\"");
        assertEquals("somePath", opt.path);
        assertEquals("a text", opt.text);
    }

    @Test
    public void testSubclassedOptionsWithShadowedOptionNameThrowsDuplicateOptionAnnotationsException() {
        class ParentOption {
            @CommandLine.Parameter(names = "-p") String path;
        }
        class ChildOption extends ParentOption {
            @CommandLine.Parameter(names = "-p") String text;
        }
        try {
            CommandLine.parse(new ChildOption(), "");
            fail("expected CommandLine$DuplicateOptionAnnotationsException");
        } catch (DuplicateOptionAnnotationsException ex) {
            assertEquals("Parameter name '-p' is used in both path and text", ex.getMessage());
        }
    }

    @Test
    public void testSubclassedOptionsWithShadowedFieldInitializesChildField() {
        class ParentOption {
            @CommandLine.Parameter(names = "-parentPath") String path;
        }
        class ChildOption extends ParentOption {
            @CommandLine.Parameter(names = "-childPath") String path;
        }
        ChildOption opt = CommandLine.parse(new ChildOption(), "-childPath", "somePath");
        assertEquals("somePath", opt.path);

        opt = CommandLine.parse(new ChildOption(), "-parentPath", "somePath");
        assertNull(opt.path);
    }
}
