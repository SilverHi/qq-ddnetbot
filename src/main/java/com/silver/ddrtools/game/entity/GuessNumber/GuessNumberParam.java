package com.silver.ddrtools.game.entity.GuessNumber;

import com.silver.ddrtools.game.entity.GameBaseParam;

import lombok.Data;

/**
 * @ClassName GuessNumberParam
 * @Description TODO
 * @Author silver
 * @Date 2022/11/16 14:46
 * @Version 1.0
 **/
@Data
public class GuessNumberParam extends GameBaseParam {
    private String answer;
    private String guessIndex;
}
