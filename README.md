*Hướng dẫn cài đặt

- Trước tiên bạn cần có STS (Spring Tools Suite), một IDE hỗ trợ các công cụ giúp chạy dự án Sring Boot back end này.
link download: https://spring.io/tools (download bản dùng cho Eclipse)

- Tiếp theo cần hệ quản trị cơ sở dữ liệu mySQL
Nếu chưa có download tại đây: 
Link vào trang download: https://dev.mysql.com/

Link tải MySQL server: https://dev.mysql.com/downloads/windo...

Link tải MySQL Workbench: https://dev.mysql.com/downloads/workb...

- Sau khi tải xong, tại MySQL Workbench tạo một script mới có nội dung: 
create database employees_management_system_database để tạo database của đồ án
- Mở STS lên và chọn workspace (nơi lưu trữ dự án) đảm bảo rằng thư mục đồ án nằm trong thư mục workspace này.
- Ở Package Explorer Click chuột phải chọn import...
- Chọn Maven, Dropdown sẽ xuất hiện -> chọn Existing Maven Projects -> Click next -> Browse đến thư mục của project -> Finish -> Đợi cho IDE download các thư viện cần thiết
- Tìm đến src/main/java/net/java/springboot/SpringbootEmsRestfullApplication.java

Trước khi chạy cần đảm bảo rằng: 
+ Đã kết nối đến hệ quản trị cơ sỡ dữ liệu mySQL
+ Đã tạo database có tên là employees_management_system_database
+ IDE download thư viện cho project đã hoàn tất
- Để chạy project click phải -> Run -> 4 (hoặc 3) Spring Boot App 
- Quá trình chạy không gặp lỗi trên console tức là code đã chạy thành công và API chạy trên local đã sẵn sàng để sử dụng cho front end.
API: http://localhost:8070/api/v1/

*Chi tiết về api

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
+ "/removeEmployeeFromDepart/{departId}/{employeeId}": Loại nhân viên ra khỏi phòng
+ "/updateEmployee/{id}": update thông tin employee
+ "/deleteAllEmployee": xóa tất cả employee
+ "/deleteEmployee/{id}": xóa nhân viên có id này

*CHI TIẾT CẬP NHẬT
+ Cập nhật thêm các method toString()
+ Ở model Department có thêm manager(kiểu Employee) với quan hệ OneToOne với department. Trong department giờ sẽ có thêm 1 trường manager
+ Ở model Employee: mặc định role của employee là "Staff", nên không cần phải có thêm trường role khi tạo mới hoặc update
+ Gender input bắt buộc phải là một trong {"male", "female", "other"}(bắt trong controller)
+ Cập nhật Thêm chức năng thăng chức trưởng phòng:
   - "/setToManager/{departId}/{employeeId}": Nếu phòng đã có trưởng phòng hoặc nhân viên muốn thăng chức này không nằm trong phòng ban này thì throws exception
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
vượt quá cho phép sẽ throws exception của mySQL.

*Update lần cuối
- Thêm sevices.
- Các controller sẽ gọi các service để giao tiếp với DB
- controller hiện giờ chỉ có nhiệm vụ gọi các Mapping