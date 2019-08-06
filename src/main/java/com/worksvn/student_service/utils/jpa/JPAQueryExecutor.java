package com.worksvn.student_service.utils.jpa;

import com.worksvn.student_service.base.models.PageDto;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map.Entry;

@Component
public class JPAQueryExecutor {
    @PersistenceContext(unitName= "primary")
    private EntityManager entityManager;

    public <T> TypedQuery<T> executeQuery(JPAQueryBuilder<T> queryBuilder) {
        TypedQuery<T> typedQuery = entityManager
                .createQuery(queryBuilder.buildQuery(), queryBuilder.getResultClass());
        for(Entry<String, Object> paramEntry : queryBuilder.getParams().entrySet()) {
            typedQuery.setParameter(paramEntry.getKey(), paramEntry.getValue());
        }
        if (queryBuilder.getPageSize() > 0) {
            typedQuery.setMaxResults(queryBuilder.getPageSize());
        }
        if (queryBuilder.getPageIndex() > 0) {
            typedQuery.setFirstResult(queryBuilder.getPageIndex() * queryBuilder.getPageSize());
        }
        return typedQuery;
    }

    public <T> PageDto<T> executePaginationQuery(JPAQueryBuilder<T> queryBuilder) {
        List<T> queryResults = executeQuery(queryBuilder).getResultList();
        long totalResult;
        if (queryBuilder.getPageSize() > 0) {
            totalResult = executeCountQuery(queryBuilder);
        } else {
            totalResult = queryResults.size();
        }
        return new PageDto<>(queryResults, queryBuilder.getPageIndex(), queryBuilder.getPageSize(), totalResult);
    }

    public <T> long executeCountQuery(JPAQueryBuilder<T> queryBuilder) {
        TypedQuery<Long> typedQuery = entityManager
                .createQuery(queryBuilder.buildCountQuery(), Long.class);
        for(Entry<String, Object> paramEntry : queryBuilder.getParams().entrySet()) {
            typedQuery.setParameter(paramEntry.getKey(), paramEntry.getValue());
        }
        if (queryBuilder.getPageSize() > 0) {
            typedQuery.setMaxResults(queryBuilder.getPageSize());
        }
        Long count = typedQuery.getSingleResult();
        return count == null? 0 : count;
    }
}
