layui.config({
	base : "/js/"
}).use(['form','layer','jquery','laypage'],function(){
	var form = layui.form(),
		layer = parent.layer === undefined ? layui.layer : parent.layer,
		laypage = layui.laypage,
		$ = layui.jquery;

	//添加旅游路线
	$(".newsAdd_btn").click(function(){
		var index = layui.layer.open({
			title : "添加旅游路线",
			type : 2,
			content : "/travelRoute_toadd",
			success : function(layero, index){
				layui.layer.tips('点击此处返回旅游路线管理列表', '.layui-layer-setwin .layui-layer-close', {
					tips: 3
				});
			}
		})
		//改变窗口大小时，重置弹窗的高度，防止超出可视区域（如F12调出debug的操作）
		$(window).resize(function(){
			layui.layer.full(index);
		})
		layui.layer.full(index);
	});


    /**
     * 01-页码跳转
     */
    $(".toPage").click(function(){
        //提交表单
        $("#pageForm").submit();
    })

    /**
     * 点击上一页
      */
    $(".previousPage").click(function(){
        if(parseInt($("#pageNumber").val())==1){
            $("#pageNumber").val(1);
            return;
        }else{
            $("#pageNumber").val(parseInt($("#pageNumber").val())-1);
            $("#pageForm").submit();
        }
    });

    /**
     * 点击下一页
     */
    $(".nextPage").click(function(){
        if(parseInt($("#pageNumber").val())==parseInt($("#size").val())){
            $("#pageNumber").val(parseInt($("#size").val()));
            return;
        }else{
            $("#pageNumber").val(parseInt($("#pageNumber").val())+1);
            $("#pageForm").submit();
        }
    })

    /**
     * 点击首页
     */
    $(".toPageOne").click(function(){
        $("#pageNumber").val(1);
        $("#pageForm").submit();
    });

    /**
     * 点击尾页
     */
    $(".toPageLast").click(function(){
        $("#pageNumber").val(parseInt($("#size").val()));
        $("#pageForm").submit();
    })

    /**
     * 切换页码
     */
    $("#pageSize").change(function(){
        $("#pageForm").submit();
    })

})
