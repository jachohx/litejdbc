# litejdbc

# 用例
* 修改好配置文件src\test\resources\service.properties里的数据库链接
* 导入src\test\resources\user.sql
* 执行org.jachohx.litejdbc下的ModelTest、JsonModelTest

# 使用
以test目录下的org.jachohx.litejdbc.User为例：

    @Table(value="user")
    @IdGenerator(value="id")
    @DbName(value="mysql")
    @ManageDate(value="true")
    public class User extends Model{
	    @PrimaryKey
	    int id;
	    String name;
	    int age;
	    @Column(column="birthday")
	    @Mapper(from="birthday")
	    Date birth;
	    int updateAt;
	    Date createAt;
	  }

* 类需要继承Model，User extends Mode
* 定义数据库名@Table(value="user") user就是表名
* 自增加ID，@IdGenerator(value="id")，非必填项
* 数据库类型@DbName(value="mysql")，目录只支持mysql，所以此项非必填
* 是否管理时间@ManageDate(value="true")，只要value != false，则托管时间，即字段updateAt、update_at、createAt、create_at。托管时间，会在insert的时候，把当前时间增加到createAt、updateAt；在update的时候，把当前时间更新到updateAt
* 主键，@PrimaryKey，如果有使用@IdGenerator，且@PrimaryKey没有填的时候，默认使用@IdGenerator
* 数据库字段与类字段对应@Column(column="birthday")，类birth字段对应数据的birthday字段。
* Model方法的使用可以参考ModelTest例子