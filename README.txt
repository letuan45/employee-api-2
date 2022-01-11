-Tên bảng ông tạo trong mySQL là: employees_management_system_database
-Khi nhập ông lưu ý:
+ email với phone tui set là unique nên nếu ông gõ record nào mà trùng 2 cái này thì nó lỗi nhá
+ Date thì tui gõ trong postman là "yyyy-mm-dd" - gõ là string
+ Attribute depart của Employee là mặc định bằng 0 (nhân viên đó chưa làm cho phòng nào)
-Tui sẽ không viết service như của fpt nhá, đọc trên mạng nghe bảo nó dùng cruid thay vì jpa..., với lại tui ko biết cái đó
nên chỉ có controller thôi
-Về controller thì có các Mapping:
+ "/getDepartments": get tất cả department
+ "/getDepartment/{id}": get department theo id
+ "/addDepartment": thêm department
+ "/updateDepartment/{id}": sửa thông tin department có id này
+ "/deleteDepartment/{id}": xóa department id này
+ "/deleteAllDepartment": xóa tất cả department

+ "/getEmployees": get tất cả employee
+ "/getEmployee/{id}": get employee có id này
+ "/addEmployee": thêm employee, đừng nhập thêm giá trị cho trường depart, nó auto = 0
+ "/addEmployeeToDepart/{departId}/{employeeId}": thêm employee vào department, cả 2 đều phải có sẵn, không có thì error
+ "/removeEmployeeFromDepart/{departId}/{employeeId}": Đuổi nhân viên ra khỏi phòng
+ "/updateEmployee/{id}": update thông tin employee
+ "/deleteAllEmployee": xóa tất cả employee
+ "/deleteEmployee/{id}": xóa nhân viên có id này

*CHI TIẾT CẬP NHẬT
+ Cập nhật thêm các method toString()
+ Ở model Department có thêm manager(kiểu Employee) với quan hệ OneToOne với department. Trong department giờ sẽ có thêm 1 trường manager
+ Ở model Employee: mặc định role của employee là "Staff", nên không cần phải có thêm trường role khi tạo mới hoặc update
+ Gender input bắt buộc phải là một trong {"male", "female", "other"}(bắt trong controller)
+ Cập nhật Thêm chức năng thăng chức trưởng phòng:
   - "/setToManager/{departId}/{employeeId}": Nếu phòng đã có trưởng phòng hoặc nhân viên muốn thăng chức này không nằm trong
phòng ban này thì throws exception
   - Sau đó update role thành "Manager" và tăng lương
+ Cập nhật Thêm chức năng giáng chức xuống nhân viên:
   - "/setToStaff/{departId}/{employeeId}": Nếu nhân viên này không phải trưởng phòng thì throw exception
   - Sau đó update role thành "Staff" và hạ lương
+ update thêm trường hợp khi update thông tin nhân viên: "/updateEmployee/{id}" -> throw error nếu role nhân viên bị thay đổi
+ update thêm trường hợp khi xóa nhân viên: "/removeEmployeeFromDepart/{departId}/{employeeId}" nếu nhân viên là trưởng phòng
thì không được xóa và sau đó throw exception.

*CHI TIẾT CẬP NHẬT LẦN 3
+ models của employee, department có thêm các length cho từng thuộc tính, và set not null cho 1 số thuộc tính
+ Tên phòng ban có tính unique
+ Set employee trong department giờ là List
+ Trong department có thêm Manager với quan hệ one-to-one 
+ Phòng ban xóa bỏ basic salary thay vào là maxEmployees và numberOfEmployees
+ Thêm 1 nhân viên đã tồn tại sẽ throw exception, thêm sẽ cần id
+ Sửa thông tin phòng ban sẽ không được sửa maxEmployees nhỏ hơn ban đầu
+ Khi xóa 1 phòng ban, các nhân viên trong phòng ban sẽ là nhân viên tự do (không có phòng ban), trưởng phòng trở thành nhân viên thường
+ Có thể xóa 1 nhân viên bất kì, cho dù đó là trưởng phòng, khi xóa xong numberOfEmployees giảm đi 1

--khi nhập json cho chức năng thêm phòng ban và sửa phòng ban chỉ cần như Sau
{
   "name": "IT",
   "maxEmployees": 4
}
--với trường numberOfEmployees sẽ được tự động tăng hoặc giảm tùy chức năng nên không cần trường này trong json.

*CHI TIẾT CẬP NHẬT LẦN 4
- Thêm thư viện validation trong pom.xml
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-validation</artifactId>
</dependency>

- Thêm validation đối với firstName và LastName của employee
+ firstName không cho phép có số và dấu khoảng trắng
+ lastName không cho phép có số và cho phép có dấu khoảng trắng

- Thêm Validation cho email và phone 
 
->> Các lỗi về validation này đều trả về trong response.data.errors: mảng các lỗi về validate

- Xử lí throws exception khi có lỗi constrain trong mySQL, các lỗi thường là vi phạm unique constain trong mySQL
lỗi này thường có trong response.data.message
+ Chưa bắt lỗi vi phạm constrain về length của dữ liệu... nếu bỏ trống ô input thì vẫn trả về error, nếu length dài
vượt quá cho phép sẽ throws exception của mySQL (cái exception dài dài như hôm qua).