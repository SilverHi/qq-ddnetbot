package com.silver.ddrtools.game.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName(value = "game_wodi")
public class GameWodi {
    private Long id;

    private String wodiWork;

    private String pingminWord;
}