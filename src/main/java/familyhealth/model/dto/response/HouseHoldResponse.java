package familyhealth.model.dto.response;

import lombok.*;
import familyhealth.common.MemberStatus;
import familyhealth.common.Relation;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HouseHoldResponse {

    private Long id;

    private Long househeadId;

    private String address;

    private Integer quantity;

    private Boolean isActive;

    private List<MemberResponse> members;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberResponse {

        private Long id;

        private String fullName;

        private Relation relation;

        private String bhyt;

        private MemberStatus memberStatus;
    }
}

