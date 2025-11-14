package familyhealth.repository.specification;

import familyhealth.model.Doctor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class DoctorSpecification {

    public static Specification<Doctor> fromSearchCriteria(String[] searchParams) {
        if (searchParams == null || searchParams.length == 0) {
            return Specification.where(null);
        }

        List<Specification<Doctor>> specs = new ArrayList<>();
        for (String param : searchParams) {
            if (StringUtils.hasText(param) && param.contains(":")) {
                String[] parts = param.split(":", 2);
                String key = parts[0];
                String value = parts[1];


                if (StringUtils.hasText(key) && StringUtils.hasText(value)) {
                    specs.add(createSpecification(key, value));
                }
            }
        }

        return Specification.allOf(specs);
    }

    private static Specification<Doctor> createSpecification(String key, String value) {
        return (root, query, criteriaBuilder) -> {
            switch (key.toLowerCase().trim()) {
                case "name":
                    return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + value.toLowerCase() + "%");
                case "specialty":
                    return criteriaBuilder.equal(root.get("specialty"), value);
                default:
                    return criteriaBuilder.conjunction();
            }
        };
    }
}
