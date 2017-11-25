var path = "/blockInfo";//path
var paginateEle = "blockInfoPage";//分页的元素
var pageSize =6;
$(function(){
	var paramPageNo = $("#paramPageNo").val();
	pageList.loadPage(paramPageNo,pageSize);
})

var pageList = {
	loadPage : function(current,pageSize){
		var paramTotal = $("#paramTotal").val();
		var pageSum = pageList.getPageSum(pageSize, paramTotal);
		page.pageInfo(current,pageSum,pageList.loadPageList,paginateEle);
	},
	loadPageList : function(current){
		var pageNo = current;
		common.post(common.getOrigin() + path,{pageNo: pageNo,pageSize: pageSize});
	},
	getPageSum : function(pageSize, pageTotal){
		var pageSum = parseInt(pageTotal / pageSize);
		pageSum = pageTotal % pageSize == 0 ? pageSum : pageSum + 1;
		return pageSum;
	}
}