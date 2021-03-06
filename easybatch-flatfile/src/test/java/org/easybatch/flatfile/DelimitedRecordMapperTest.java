/*
 * The MIT License
 *
 *  Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package org.easybatch.flatfile;

import org.easybatch.core.record.StringRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Unit test class for {@link DelimitedRecordMapper}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
@RunWith(MockitoJUnitRunner.class)
public class DelimitedRecordMapperTest {

    private DelimitedRecordMapper delimitedRecordMapper;

    @Mock
    private StringRecord record, headerRecord;

    @Before
    public void setUp() throws Exception {
        delimitedRecordMapper = new DelimitedRecordMapper<Person>(Person.class,
                new String[]{"firstName", "lastName", "age", "birthDate", "married"});

        when(record.getPayload()).thenReturn("foo,bar,30,1990-12-12,true");
        when(headerRecord.getPayload()).thenReturn("firstName,lastName,age,birthDate,married");
    }

    /*
     * Record parsing tests
     */

    @Test(expected = Exception.class)
    public void testIllFormedRecord() throws Exception {
        when(record.getPayload()).thenReturn("foo,bar,30,1990-12-12");// incorrect record size
        delimitedRecordMapper.parseRecord(record);
    }


    @Test
    public void testRecordSizeWithEmptyField() throws Exception {
        when(record.getPayload()).thenReturn("foo,bar,30,1990-12-12,");
        FlatFileRecord flatFileRecord = delimitedRecordMapper.parseRecord(record);
        assertThat(flatFileRecord.getFlatFileFields().get(4).getRawContent()).isEmpty();
    }

    @Test
    public void testRecordParsing() throws Exception {
        validateRecord(record);
    }

    @Test
    public void testRecordParsingWithTrimmedWhitespaces() throws Exception {
        delimitedRecordMapper.setTrimWhitespaces(true);
        when(record.getPayload()).thenReturn("  foo ,    bar  ,  30  ,     1990-12-12  ,  true         ");
        validateRecord(record);
    }

    @Test
    public void testRecordParsingWithPipeDelimiter() throws Exception {
        delimitedRecordMapper.setDelimiter("|");
        when(record.getPayload()).thenReturn("foo|bar|30|1990-12-12|true");
        validateRecord(record);
    }

    @Test
    public void testRecordParsingWithSpaceDelimiter() throws Exception {
        delimitedRecordMapper.setDelimiter(" ");
        when(record.getPayload()).thenReturn("foo bar 30 1990-12-12 true");
        validateRecord(record);
    }

    @Test
    public void testRecordParsingWithTabDelimiter() throws Exception {
        delimitedRecordMapper.setDelimiter("\t");
        when(record.getPayload()).thenReturn("foo\tbar\t30\t1990-12-12\ttrue");
        validateRecord(record);
    }

    @Test
    public void testRecordParsingWithMultipleCharactersDelimiter() throws Exception {
        delimitedRecordMapper.setDelimiter("###");
        when(record.getPayload()).thenReturn("foo###bar###30###1990-12-12###true");
        validateRecord(record);
    }

    @Test
    public void testRecordParsingWithSimpleQuoteQualifier() throws Exception {
        delimitedRecordMapper.setQualifier("'");
        when(record.getPayload()).thenReturn("'foo','bar','30','1990-12-12','true'");
        validateRecord(record);
    }

    @Test(expected = Exception.class)
    public void allFieldsShouldBeQualified() throws Exception {
        delimitedRecordMapper.setQualifier("'");
        when(record.getPayload()).thenReturn("'foo','bar',30,'1990-12-12','true'"); //age field not qualified
        validateRecord(record);
    }

    @Test
    public void testRecordParsingWithDoubleQuoteQualifier() throws Exception {
        delimitedRecordMapper.setQualifier("\"");
        when(record.getPayload()).thenReturn("\"foo\",\"bar\",\"30\",\"1990-12-12\",\"true\"");
        validateRecord(record);
    }

    @Test
    public void testFieldSubsetMapping() throws Exception {
        delimitedRecordMapper = new DelimitedRecordMapper<Person>(Person.class,
                new Integer[]{0, 4},
                new String[]{"firstName", "married"}
        );
        FlatFileRecord flatFileRecord = delimitedRecordMapper.parseRecord(record);
        assertThat(flatFileRecord).isNotNull();
        assertThat(flatFileRecord.getFlatFileFields().size()).isEqualTo(2);
        assertThat(flatFileRecord.getFlatFileFields().get(0).getRawContent()).isEqualTo("foo");
        assertThat(flatFileRecord.getFlatFileFields().get(1).getRawContent()).isEqualTo("true");
    }

    private void validateRecord(final StringRecord stringRecord) throws Exception {
        FlatFileRecord flatFileRecord = delimitedRecordMapper.parseRecord(stringRecord);
        assertThat(flatFileRecord).isNotNull();
        List<FlatFileField> flatFileFields = flatFileRecord.getFlatFileFields();
        assertThat(flatFileFields.size()).isEqualTo(5);
        assertThat(flatFileFields.get(0).getRawContent()).isEqualTo("foo");
        assertThat(flatFileFields.get(1).getRawContent()).isEqualTo("bar");
        assertThat(flatFileFields.get(2).getRawContent()).isEqualTo("30");
        assertThat(flatFileFields.get(3).getRawContent()).isEqualTo("1990-12-12");
        assertThat(flatFileFields.get(4).getRawContent()).isEqualTo("true");
    }

     /*
     * Record mapping tests
     */

    @Test
    public void testFieldNamesConventionOverConfiguration() throws Exception {
        delimitedRecordMapper = new DelimitedRecordMapper<Person>(Person.class);

        delimitedRecordMapper.parseRecord(headerRecord);
        Person person = (Person) delimitedRecordMapper.mapRecord(record);
        assertThat(person).isNotNull();
        assertThat(person.getFirstName()).isEqualTo("foo");
        assertThat(person.getLastName()).isEqualTo("bar");
        assertThat(person.getAge()).isEqualTo(30);
        assertThat(person.getBirthDate()).isEqualTo(java.sql.Date.valueOf("1990-12-12"));
        assertThat(person.isMarried()).isTrue();
    }

    @Test
    public void testFieldSubsetMappingWithConventionOverConfiguration() throws Exception {
        delimitedRecordMapper = new DelimitedRecordMapper<Person>(Person.class, new Integer[]{0, 4});
        delimitedRecordMapper.parseRecord(headerRecord);
        Person person = (Person) delimitedRecordMapper.mapRecord(record);

        assertThat(person).isNotNull();
        assertThat(person.getFirstName()).isEqualTo("foo");
        assertThat(person.getLastName()).isNull();
        assertThat(person.getAge()).isEqualTo(0);
        assertThat(person.getBirthDate()).isNull();
        assertThat(person.isMarried()).isTrue();
    }

}
