package site.metacoding.red.web.dto.request.users;


import lombok.Getter;
import lombok.Setter;
import site.metacoding.red.domain.boards.Boards;

@Setter
@Getter // setter는 안쓸 수 있으면 가급적 안쓰는 것이 좋음
public class UpdateDto {
	private String password;
	private String email;
	

}
