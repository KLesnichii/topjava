package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudMealRepository extends JpaRepository<Meal, Integer> {

    @Query("SELECT m FROM Meal m WHERE m.user.id=:userId")
    List<Meal> getAll(@Param("userId") int userId, Sort sort);

    @Query("SELECT m FROM Meal m " +
            "WHERE m.user.id=:userId AND m.dateTime >= :startDateTime AND m.dateTime < :endDateTime")
    List<Meal> getBetweenHalfOpen(@Param("userId") int userId,
                                  @Param("startDateTime") LocalDateTime startDateTime,
                                  @Param("endDateTime") LocalDateTime endDateTime, Sort sort);

    Meal findByIdAndUserId(@Param("id") int id, @Param("userId") int userId);

    @Query("SELECT m FROM Meal m INNER JOIN FETCH m.user WHERE m.id=:id AND m.user.id=:userId ")
    Meal findWithUser(@Param("id") int id, @Param("userId") int userId);

    @Transactional
    @Modifying
    @Query("update Meal m set m.description =:description,m.calories =:calories,m.dateTime=:dateTime " +
            "WHERE m.id=:id AND m.user.id=:userId")
    int update(@Param("id") int id,
               @Param("userId") int userId,
               @Param("dateTime") LocalDateTime dateTime,
               @Param("description") String description,
               @Param("calories") int calories);

    @Transactional
    @Modifying
    @Query("DELETE FROM Meal m WHERE m.id=:id AND m.user.id=:userId")
    int delete(@Param("id") int id, @Param("userId") int userId);
}