# litejdbc

# ����
* �޸ĺ������ļ�src\test\resources\service.properties������ݿ�����
* ����src\test\resources\user.sql
* ִ��org.jachohx.litejdbc�µ�ModelTest��JsonModelTest

# ʹ��
��testĿ¼�µ�org.jachohx.litejdbc.UserΪ����

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

* ����Ҫ�̳�Model��User extends Mode
* �������ݿ���@Table(value="user") user���Ǳ���
* ������ID��@IdGenerator(value="id")���Ǳ�����
* ���ݿ�����@DbName(value="mysql")��Ŀ¼ֻ֧��mysql�����Դ���Ǳ���
* �Ƿ����ʱ��@ManageDate(value="true")��ֻҪvalue != false�����й�ʱ�䣬���ֶ�updateAt��update_at��createAt��create_at���й�ʱ�䣬����insert��ʱ�򣬰ѵ�ǰʱ�����ӵ�createAt��updateAt����update��ʱ�򣬰ѵ�ǰʱ����µ�updateAt
* ������@PrimaryKey�������ʹ��@IdGenerator����@PrimaryKeyû�����ʱ��Ĭ��ʹ��@IdGenerator
* ���ݿ��ֶ������ֶζ�Ӧ@Column(column="birthday")����birth�ֶζ�Ӧ���ݵ�birthday�ֶΡ�
* Model������ʹ�ÿ��Բο�ModelTest����