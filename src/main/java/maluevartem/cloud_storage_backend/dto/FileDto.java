package maluevartem.cloud_storage_backend.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileDto {

    private String fileName;
    private String fileType;
    private byte[] fileData;
    private String hash;
    private Long size;

}
