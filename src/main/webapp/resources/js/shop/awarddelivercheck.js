$(function() {
    var awardName = '';

    function getList() {
        //获取记录积分兑换的URL
        var listUrl = '/o2o/shopadmin/listuserawardmapsbyshop?pageIndex=1&pageSize=9999' + '&awardName=' + awardName;
        $.getJSON(listUrl, function(data) {
            if (data.success) {
                var userAwardMapList = data.userAwardMapList;
                var tempHtml = '';
                //拼接展示
                userAwardMapList.map(function(item, index) {
                    tempHtml += '' + '<div class="row row-awarddeliver">'
                        + '<div class="col-10">' + item.award.awardName
                        + '</div>'
                        + '<div class="col-40 awarddeliver-time">'
                        + new Date(item.createTime).Format("yyyy-MM-dd hh:mm:ss")
                        + '</div>' + '<div class="col-20">' + item.user.name
                        +'</div>'+'<div class="col-10">'+item.point
                        +'</div> '+'<div class="col-20">'+item.operator.name+'</div> '
                        + '</div>';
                });
                $('.awarddeliver-wrap').html(tempHtml);
            }
        });
    }

    //搜索绑定，获取并按照奖品模糊查询
    $('#search').on('change', function(e) {
        awardName = e.target.value;
        $('.awarddeliver-wrap').empty();
        getList();
    });

    getList();
});