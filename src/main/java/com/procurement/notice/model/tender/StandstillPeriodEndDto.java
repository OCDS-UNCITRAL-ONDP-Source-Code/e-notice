package com.procurement.notice.model.tender;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.procurement.notice.model.ocds.*;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "awardPeriod",
        "awards",
        "lots",
        "bids"
})
public class StandstillPeriodEndDto {

    @JsonProperty("awardPeriod")
    private final Period awardPeriod;
    @JsonProperty("awards")
    private final List<Award> awards;
    @JsonProperty("lots")
    private final List<Lot> lots;
    @JsonProperty("bids")
    private final List<Bid> bids;

    @JsonCreator
    public StandstillPeriodEndDto(@JsonProperty("awardPeriod") final Period awardPeriod,
                                  @JsonProperty("awards") final List<Award> awards,
                                  @JsonProperty("lots") final List<Lot> lots,
                                  @JsonProperty("bids") final List<Bid> bids) {
        this.awardPeriod = awardPeriod;
        this.awards = awards;
        this.lots = lots;
        this.bids = bids;
    }
}
