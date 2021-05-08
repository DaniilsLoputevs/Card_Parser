import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class Example {
    public class Candidate {
        @NotNull int id;
        @Nullable String name;
        @NotNull int photoId;
    }
    
    
    public class CandidateNew {
        @NotNull int id;
        @Nullable String name;
        @Nullable int photoId;
    }
  
}
