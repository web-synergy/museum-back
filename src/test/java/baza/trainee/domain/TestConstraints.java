package baza.trainee.domain;

import baza.trainee.constants.EventConstant;

public class TestConstraints {

    public static final String UNDERSIZED_TITLE = "t".repeat(EventConstant.MIN_TITLE_SIZE - 1);
    public static final String UNDERSIZED_DESCRIPTION = "d".repeat(EventConstant.MIN_DESCRIPTION_SIZE - 1);
    public static final String OVERSIZED_TITLE = "t".repeat(EventConstant.MAX_TITLE_SIZE + 1);
    public static final String OVERSIZED_DESCRIPTION = "t".repeat(EventConstant.MAX_DESCRIPTION_SIZE + 1);

}
