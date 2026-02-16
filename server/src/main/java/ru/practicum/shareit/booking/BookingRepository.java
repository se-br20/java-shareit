package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    boolean existsByItem_IdAndBooker_IdAndStatusAndEndIsBefore(
            Long itemId, Long userId, BookingStatus status, LocalDateTime time
    );

    @Query("""
            select b from Booking b
            join fetch b.item i
            join fetch b.booker u
            where b.id = ?1
            """)
    Optional<Booking> findFullById(Long bookingId);

    List<Booking> findByBooker_Id(Long bookerId, Sort sort);

    List<Booking> findByBooker_IdAndStartIsBeforeAndEndIsAfter(
            Long bookerId, LocalDateTime now1, LocalDateTime now2, Sort sort
    );

    List<Booking> findByBooker_IdAndEndIsBefore(Long bookerId, LocalDateTime now, Sort sort);

    List<Booking> findByBooker_IdAndStartIsAfter(Long bookerId, LocalDateTime now, Sort sort);

    List<Booking> findByBooker_IdAndStatus(Long bookerId, BookingStatus status, Sort sort);

    List<Booking> findByItem_Owner_Id(Long ownerId, Sort sort);

    List<Booking> findByItem_Owner_IdAndStartIsBeforeAndEndIsAfter(
            Long ownerId, LocalDateTime now1, LocalDateTime now2, Sort sort
    );

    List<Booking> findByItem_Owner_IdAndEndIsBefore(Long ownerId, LocalDateTime now, Sort sort);

    List<Booking> findByItem_Owner_IdAndStartIsAfter(Long ownerId, LocalDateTime now, Sort sort);

    List<Booking> findByItem_Owner_IdAndStatus(Long ownerId, BookingStatus status, Sort sort);

    @Query("""
            select b from Booking b
            join fetch b.item i
            join fetch b.booker u
            where i.id in ?1
              and b.status = ?2
            """)
    List<Booking> findApprovedForItems(List<Long> itemIds, BookingStatus status);

    @Query("""
            select b from Booking b
            join fetch b.booker u
            where b.item.id = ?1
              and b.status = ?2
              and b.start <= ?3
            order by b.start desc
            """)
    List<Booking> findLastApprovedForItem(Long itemId, BookingStatus status, LocalDateTime now, Pageable pageable);

    @Query("""
            select b from Booking b
            join fetch b.booker u
            where b.item.id = ?1
              and b.status = ?2
              and b.start > ?3
            order by b.start asc
            """)
    List<Booking> findNextApprovedForItem(Long itemId, BookingStatus status, LocalDateTime now, Pageable pageable);
}