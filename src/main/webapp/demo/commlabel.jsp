<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/inc/inc.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>demo</title>
</head>

<script type="text/javascript">
(function () {
	seajs.use('app/demo/commlabel', function (DemoPager) {
		var pager = new DemoPager({element: document.body});
// 		pager.initialize();
		pager.render();
	});
})();
var relationMap = {"WATER":{"北京":["北京"],"太阳系国":[],"重庆":["重庆"],"新疆":[],"广东":["东莞","广州","梅州","深圳","珠海"],"天津":[],"浙江":["东阳","杭州","嘉兴","金华","宁波","绍兴","台州","温州","义乌"],"省份":[],"深圳":[],"广西":["南宁"],"内蒙古":["赤峰"],"江西":["九江","南昌","新余"],"安徽":["蚌埠","合肥","淮北","淮南","黄山"],"陕西":["西安"],"辽宁":["鞍山","大连","沈阳","营口"],"山西":["太原"],"四川":["成都"],"江苏":["淮安","江都","江阴","南京","南通","苏州","宿迁","无锡","徐州","扬州","镇江"],"河北":["石家庄"],"福建":["福州","莆田","泉州","厦门"],"吉林":["吉林"],"云南":["昆明"],"湖北":["武汉"],"海南":[],"上海":["上海"],"全国":[],"湖南":["长沙","衡阳","湘潭"],"山东":["济南","青岛","潍坊","烟台"],"河南":["开封","洛阳","信阳","岳阳","郑州"],"黑龙江":["哈尔滨"]},"GAS":{"北京":["北京"],"太阳系国":[],"重庆":["重庆"],"新疆":[],"广东":["东莞","佛山","广州","深圳"],"天津":[],"浙江":["杭州","宁波","温州","浙江全省"],"省份":[],"深圳":[],"广西":[],"内蒙古":["呼和浩特"],"江西":["南昌","新余"],"安徽":["合肥","淮北"],"陕西":["西安"],"辽宁":["鞍山","朝阳","大连","沈阳"],"山西":[],"四川":["成都"],"江苏":["淮安","南京","南通","苏州","无锡","徐州","扬州","宜兴","镇江"],"河北":["石家庄"],"福建":["福州"],"吉林":[],"云南":["昆明"],"湖北":[],"海南":["海口"],"上海":["上海"],"全国":[],"湖南":[],"山东":["济南","青岛","潍坊","烟台"],"河南":["开封","洛阳","郑州"],"黑龙江":["哈尔滨"]},"ELECTRIC":{"北京":["北京"],"太阳系国":[],"重庆":["重庆"],"新疆":[],"广东":["潮州","东莞","佛山","广州","河源","惠州","江门","揭阳","梅州","汕尾","深圳","中山","珠海"],"天津":[],"浙江":["杭州","湖州","嘉兴","金华","丽水","宁波","衢州","绍兴","台州","温州","舟山"],"省份":[],"深圳":[],"广西":["北海","防城港","贵港","桂林","河池","来宾","柳州","南宁","玉林"],"内蒙古":["阿拉善盟","巴彦淖尔","包头","赤峰","鄂尔多斯","呼和浩特","呼伦贝尔","通辽","乌海","乌兰察布","锡林郭勒","兴安盟","准格尔"],"江西":["抚州","赣州","吉安","景德镇","九江","南昌","萍乡","上饶","新余","宜春","鹰潭"],"贵州":["安顺","毕节","都匀","贵阳","凯里","六盘水","铜仁","兴义","遵义"],"安徽":["安徽全省","安庆","蚌埠","滁州","合肥","淮北","淮南","黄山","宿州"],"陕西":["西安"],"辽宁":["鞍山","本溪","朝阳","大连","丹东","抚顺","阜新","葫芦岛","盘锦","沈阳","铁岭","营口"],"山西":["太原"],"青海":["西宁"],"四川":["成都","绵阳","自贡"],"江苏":["常州","淮安","江苏全省","连云港","南京","南通","苏州","宿迁","泰州","无锡","徐州","盐城","扬州","镇江"],"福建":["福州","龙岩","南平","宁德","莆田","泉州","三明","厦门","漳州"],"吉林":["白城","白山","长春","吉林","辽源","四平","松原","通化","延边州延吉"],"上海":["上海"],"云南":["昆明"],"湖北":["鄂州","湖北全省","黄冈","黄石","荆门","荆州","十堰","随州","武汉","仙桃","咸宁","襄樊","孝感","宜昌"],"海南":["海口"],"全国":[],"甘肃":["白银","甘南","嘉峪关","金昌","酒泉","张掖"],"湖南":["长沙","常德","衡阳","湖南全省"],"山东":["济南","青岛","潍坊","烟台"],"河南":["安阳","鹤壁","济源","焦作","开封","洛阳","漯河","南阳","平顶山","濮阳","三门峡","商丘","新乡","信阳","许昌","郑州","周口","驻马店"],"黑龙江":["哈尔滨","黑龙江全省"]}};


function parseProv(type) {
    var o = relationMap[type], result = [];
    result.push({value:'',text:'请选择', selected: true})
    for (i in o) {
        var prov = i;
        result.push({value:prov,text:prov, selected: false})
    }
    return result;
}
function parseCity(type, prov) {
    var o = relationMap[type], cities = o[prov], result = [];
    result.push({value:'',text:'请选择', selected: true})
    for (i in cities) {
        var city = cities[i];
        result.push({value:city,text:city, selected: false})
    }
    return result;
}
</script>
<body>
1 日历: <input id="date-nothing" type="text" /> <br/> <br/>

2 基本弹出框: <input type='button' value="基本弹出框" id="baseDialog"/> <br/> <br/>

3 标准模态对话框 <input type='button' value="标准模态对话框" id="standDialog"/> <br/> <br/>

4 提示框  <input type="button" value='提示框' id="tip1"/> <br/><br/>

5 下拉框 <a href="#" id="example2" class="ui-select-trigger">请选择</a> <br/><br/>

6 级联 <p><select id="exampel6-1">
    <option value="WATER">水费</option>
    <option value="ELECTRIC">电费</option>
</select>
<a href="#" id="exampel6-2" class="ui-select-trigger">
    <span data-role="trigger-content">请选择</span>
    <i class="iconfont" title="下三角形">&#xF03C;</i>
</a>
<a href="#" id="exampel6-3" class="ui-select-trigger">
    <span data-role="trigger-content">请选择</span>
    <i class="iconfont" title="下三角形">&#xF03C;</i>
</a></p>  </br></br>




7  自动补全 <input id="acTrigger1" type="text" value="" /> <br></br>

9  Tab 
 <div id="tab-demo-1" class="tab-demo">
    <ul class="ui-switchable-nav">
        <li>标题 A</li>
        <li>标题 B</li>
        <li>标题 C</li>
        <li>标题 D</li>
    </ul>
    <div class="ui-switchable-content">
        <div>
            内容 A
            <pre>
              - 在当前 trigger 中 mouseover/mouseout, click, focus, 不触发
              - 鼠标快速滑过非当前 trigger, 不触发
              - mouseover 到非当前 trigger, 停留时间到达延迟时，触发
              - click 或 Tab 切换到 trigger, 立即触发
              - switch / switched 事件的触发
            </pre>
        </div>
        <div >内容 B</div>
        <div >内容 C</div>
        <div >内容 D</div>
    </div>
</div>

10 分页控件
<link rel="stylesheet" href="http://static.nimojs.com/umd/alice-paging/1.1.0/index.css">
<div id="paging-demo"></div>
<br><br>

10 Slide
<div id="slide-demo-1" class="slide-demo">
    <ul class="ui-switchable-content" data-role="content">
        <li class="ui-switchable-panel"><a href="#"><img src="./assets/slide_1.jpg" /></a></li>
        <li class="ui-switchable-panel"><a href="#"><img src="./assets/slide_2.jpg" /></a></li>
        <li class="ui-switchable-panel"><a href="#"><img src="./assets/slide_3.jpg" /></a></li>
    </ul>
</div>

11 二维码



<div id="qrcodeDefault"></div>
</body>
</html>