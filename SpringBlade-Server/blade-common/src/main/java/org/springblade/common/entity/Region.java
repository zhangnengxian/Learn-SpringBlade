package org.springblade.common.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author zhangnx
 * 获取行政区划信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "blade_region")
public class Region implements Serializable {

	@TableId(value = "id", type = IdType.INPUT)
	private Integer id;

	@ApiModelProperty(value = "行政区划代码")
	public String code;

	@ApiModelProperty(value = "行政区划名称")
	public String name;

	@ApiModelProperty(value = "行政区划级别")
	public int level;

	@ApiModelProperty(value = "孩子节点")
	public List<Region> child;
	/**
	 * 父辖区编码
	 */
	@ApiModelProperty(value = "父辖区编码", required = true)
	private String fatherCode;

	/**;
	 * 辖区名称路径,例如广东省/广州市
	 */
	@ApiModelProperty(value = "辖区名称路径,例如广东省/广州市", required = true,example = "广东省/广州市/天河区")
	private String namePath;

	/**
	 * 辖区id路径,例如 1/2/3
	 */
	@ApiModelProperty(value = "辖区id路径,例如 1/2/3", required = true)
	private String idPath;


	@ApiModelProperty(value = "创建时间", required = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	@ApiModelProperty(value = "更新时间", required = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;

    public Region(String code, String name, int level) {
        this.code = code;
        this.name = name;
        this.level = level;
    }

}
