package com.utils;

import com.security.entities.User;
import com.tasks.entities.TaskBoard;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SpecificationUtils {
    public static Specification<TaskBoard> getTaskBoardsSpecification(Map<String, String> filters){
        return (Specification<TaskBoard>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            for(Map.Entry<String, String> filter: filters.entrySet()){
                if(StringUtils.isEmpty(filter.getValue())){
                    continue;
                }

                if(filter.getKey().equals("currentUserId")){
                    Join usersJoin = root.join("boardUsers", JoinType.INNER);
                    predicates.add(criteriaBuilder.equal(usersJoin.get("userId"), Long.valueOf(filter.getValue())));
                } else {
                    predicates.add(criteriaBuilder.or(criteriaBuilder.like(criteriaBuilder.upper(root.get(filter.getKey())), "%" + filter.getValue().toUpperCase() + "%"),
                            criteriaBuilder.like(criteriaBuilder.lower(root.get(filter.getKey())), "%" + filter.getValue().toLowerCase() + "%"),
                            criteriaBuilder.like(criteriaBuilder.upper(root.get(filter.getKey())), filter.getValue().toUpperCase() + "%"),
                            criteriaBuilder.like(criteriaBuilder.upper(root.get(filter.getKey())), "%"  + filter.getValue().toUpperCase())));
                }
            }

            return criteriaBuilder.and(SpecificationUtils.predicatesToArray(predicates));
        };
    }

    public static Specification<User> getUsersSpecification(Map<String, String> filters){
        return (Specification<User>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            for(Map.Entry<String, String> filter: filters.entrySet()){
                if(StringUtils.isEmpty(filter.getValue())){
                    continue;
                }

                String value = filter.getValue().toLowerCase();
                predicates.add(criteriaBuilder.or(criteriaBuilder.like(criteriaBuilder.lower(root.get(filter.getKey())), "%" + value + "%"),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get(filter.getKey())), value + "%"),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get(filter.getKey())), "%"  + value))); }

            return criteriaBuilder.and(SpecificationUtils.predicatesToArray(predicates));
        };
    }

    public static Predicate[] predicatesToArray(List<Predicate> predicates){
        Predicate[] predicatesArray = new Predicate[predicates.size()];
        int predicateIndex = 0;

        for(Predicate predicate: predicates){
            predicatesArray[predicateIndex] = predicate;
        }

        return predicatesArray;
    }
}
