package com.procurement.notice.dto;

import java.util.List;
import lombok.Data;

@Data
public class LotGroup {
    public String id;
    public List<String> relatedLots;
    public Boolean optionToCombine;
    public Value maximumValue;
}
