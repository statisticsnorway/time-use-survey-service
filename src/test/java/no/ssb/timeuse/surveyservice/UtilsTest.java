package no.ssb.timeuse.surveyservice;

import no.ssb.timeuse.surveyservice.activitiy.ActivityCategoryService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UtilsTest {

    /**
     * A activity code contains four elements, where the first is a double digit between 0-99, followed by 3 digits between 0-9, separated with a dot.
     */
    final String code_true_0 = "01";
    final String code_true_1 = "01.1";
    final String code_true_2 = "01.1.9";
    final String code_true_3 = "02.9.9.9";
    final String code_false_0 = "";
    final String code_false_1 = "v1";
    final String code_false_2 = "2.2.2";
    final String code_false_3 = "v3.3.3";
    final String code_false_4 = "02.9.9.19";
    final String code_false_5 = "1,1";
    final String code_false_6 = "1.1";
    final String code_false_7 = "1.1.1.1.1";

    /**
     * An approved description of Activity has to contain a text (String)
     */
    final String descript_true_0 = "melk";
    final String descript_true_1 = "BRØD";
    final String descript_true_2 = "melk (laktose)";
    final String descript_true_3 = "Melk 2 liter";
    final String descript_false_0 = "";



    @Test
    void isCode() {
        assertTrue(ActivityCategoryService.isCode(code_true_0));
        assertTrue(ActivityCategoryService.isCode(code_true_1));
        assertTrue(ActivityCategoryService.isCode(code_true_2));
        assertTrue(ActivityCategoryService.isCode(code_true_3));
        assertFalse(ActivityCategoryService.isCode(code_false_0));
        assertFalse(ActivityCategoryService.isCode(code_false_1));
        assertFalse(ActivityCategoryService.isCode(code_false_2));
        assertFalse(ActivityCategoryService.isCode(code_false_3));
        assertFalse(ActivityCategoryService.isCode(code_false_4));
        assertFalse(ActivityCategoryService.isCode(code_false_5));
        assertFalse(ActivityCategoryService.isCode(code_false_6));
        assertFalse(ActivityCategoryService.isCode(code_false_7));
    }

    @Test
    void isDescription() {
        assertTrue(ActivityCategoryService.isDescription(descript_true_0));
        assertTrue(ActivityCategoryService.isDescription(descript_true_1));
        assertTrue(ActivityCategoryService.isDescription(descript_true_2));
        assertTrue(ActivityCategoryService.isDescription(descript_true_3));
        assertFalse(ActivityCategoryService.isDescription(descript_false_0));
    }

}
