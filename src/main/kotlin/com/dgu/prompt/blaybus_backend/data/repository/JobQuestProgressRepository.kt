import com.dgu.prompt.blaybus_backend.data.entity.JobQuest
import org.springframework.data.jpa.repository.JpaRepository

interface JobQuestRepository : JpaRepository<JobQuest, Int>
