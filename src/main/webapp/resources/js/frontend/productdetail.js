$(function() {
    //从地址栏的URL里获取productId
    var productId = getQueryString('productId');
    //获取商品的URL
    var productUrl = '/o2o/frontend/listproductdetailpageinfo?productId='
        + productId;
    //访问后台获取该商品的信息并渲染
    $
        .getJSON(
            productUrl,
            function(data) {
                if (data.success) {
                    //获取商品信息
                    var product = data.product;
                    //给商品信息相关HTML控件赋值
                    //商品缩略图
                    $('#product-img').attr('src', getContexPath() + product.imgAddr);
                    //商品更新时间
                    $('#product-time').text(
                        new Date(product.lastEditTime)
                            .Format("yyyy-MM-dd"));
                    if (product.point != undefined) {
                        $('#product-point').text(
                            '购买可得' + product.point + '积分'
                        );
                    }
                    //商品名称
                    $('#product-name').text(product.productName);
                    $('#product-desc').text(product.productDesc);
                    //商品价格展示逻辑，主要判断原价现价是否为空，所有都为空则不显示价格栏目
                    if (product.normalPrice != undefined
                        && product.promotionPrice != undefined) {
                        //如果原价和现价都不为空则都展示，并且给原价加个删除符号
                        $('#price').show();
                        $('#normalPrice').html(
                            '<del>' + '￥' + product.normalPrice
                            + '</del>'
                        );
                        $('#promotionPrice').text(
                            '￥' + product.promotionPrice
                        );
                    }else if (product.normalPrice != undefined && product.promotionPrice == undefined) {
                        //若原价不为空现在为空则只展示原价
                        $('#price').show();
                        $('#promotionPrice').text(
                            '￥' + product.promotionPrice
                        );
                    }else if (product.normalPrice == undefined && product.promotionPrice != undefined) {
                        //若现价不为空原价为空则只展示现价
                        $('#promotionPrice').text(
                            '￥' + product.promotionPrice
                        );
                    }
                    var imgListHtml = '';
                    product.productImgList.map(function (item, index) {
                        imgListHtml += '<div> <img src="'
                        getContexPath() + item.imgAddr + '"width="100%"/></div>';
                    });

                    //生成购买商品的二维码供商家扫描
                    if (data.needQRCode) {
                        //顾客已登陆，则生成购买商品的二维码供商家扫描
                        imgListHtml += '<div> <img src="/o2o/frontend/generateqrcode4product?productId='
                            + product.productId + '"width="100%"/></div>';
                        $('#imgList').html(imgListHtml);
                    }
                }
            });
    $('#me').click(function() {
        $.openPanel('#panel-left-demo');
    });
    $.init();
});
