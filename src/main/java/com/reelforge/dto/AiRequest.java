package com.reelforge.dto;

import lombok.Data;

@Data
public class AiRequest {

    private String topic;
    private String niche;
    private String platform;
    private String tone;
}
