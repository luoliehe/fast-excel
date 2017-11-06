package org.utils.fastexcel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.utils.fastexcel.annotation.EaseCell;
import org.utils.fastexcel.annotation.EaseRow;
import org.utils.fastexcel.validate.NotEmpty;
import org.utils.fastexcel.validate.Phone;

@EaseRow("Excel模板")
public class Bean {

	@NotEmpty(message = "姓名不能为空")
	@EaseCell(index = 0, name = "name", label = "姓名")
	private String name;

	@NotEmpty(message = "省不能为空")
	@EaseCell(index = 1, name = "province", label = "省")
	private String province;

	@NotEmpty(message = "城市不能为空")
	@EaseCell(index = 2, name = "city", label = "市")
	private String city;

	@Phone(message = "手机号码格式不正确")
	@NotEmpty(message = "电话号码不能为空")
	@EaseCell(index = 3, name = "phone", label = "电话")
	private String phone;

	@EaseCell(index = 4, name = "suppliername", label = "供应商")
	private String suppliername;

	@EaseCell(index = 5, name = "series", label = "车系")
	private String series;

	@EaseCell(index = 6, name = "model", label = "型号")
	private String model;

	@EaseCell(index = 7, name = "sex", label = "性别", format = GenderFormat.class)
	private String sex;

	@EaseCell(index = 8, name = "email", label = "邮箱")
	private String email;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSuppliername() {
		return suppliername;
	}

	public void setSuppliername(String suppliername) {
		this.suppliername = suppliername;
	}

	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Row2Bean [name=" + name + ", province=" + province + ", city=" + city + ", phone=" + phone
				+ ", suppliername=" + suppliername + ", series=" + series + ", sex=" + sex + ", email=" + email + "]";
	}

	public static class GenderFormat implements CellFormat {

		@Override
		public Object format(Cell cell) {
			cell.setCellType(CellType.STRING);
			return "自定义性别格式化" + cell.getStringCellValue();
		}

	}

}
