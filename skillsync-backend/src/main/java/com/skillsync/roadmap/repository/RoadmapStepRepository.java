@Modifying
@Transactional
@Query("DELETE FROM RoadmapStep r WHERE r.id = :stepId AND r.user = :user")
void deleteByIdAndUser(@Param("stepId") Long stepId, @Param("user") User user);