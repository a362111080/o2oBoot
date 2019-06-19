$(function() {
    var productName = '';
    getProductSellDailyList()
    getList();
    function getList() {
        var listUrl = '/o2o/shopadmin/listuserproductmapsbyshop?pageIndex=1&pageSize=9999&shopName=' + productName;
        //访问后台，获取该店铺的购买信息列表
        $.getJSON(listUrl, function (data) {
            if (data.success) {
                var userProductMapList = data.userProductMapList;
                var tempHtml = '';
                //遍历购买信息列表，拼接出列信息
                userProductMapList.map(function (item, index) {
                    tempHtml += ''
                        +      '<div class="row row-productbuycheck">'
                        +          '<div class="col-10">'+ item.product.productName +'</div>'
                        +          '<div class="col-40 productbuycheck-time">'+ new Date(item.createTime).Format("yyyy-MM-dd hh:mm:ss") +'</div>'
                        +          '<div class="col-20">'+ item.user.name +'</div>'
                        +           '<div class="col-10">'+item.point+'</div> '
                        +           '<div class="col-20" '+item.operator.name+'</div> '
                        +      '</div>';
                });
                $('.productbuycheck-wrap').html(tempHtml);
            }
        });
    }

    $('#search').on('change', function (e) {
        //当在搜索框里输入信息的时候
        //依据输入的商品名模糊查询该商品的购买记录
        productName = e.target.value;
        //清空购买商品记录列表
        $('.productbuycheck-wrap').empty();
        //再次加载
        getList();
    });
    /**
     * 获取7天的销量
     */
    function getProductSellDailyList() {
        //获取该店铺商品7天销量的URL
        var listProductSellDilyUrl = '/o2o/shopadmin/listproductselldailyinfobyshop';
        //访问后台，该店铺商品7天销量的URL
        $.getJSON(listProductSellDilyUrl, function (data) {
            if (data.success) {
                var myChart = echarts.init(document.getElementById("chart"));
                //生成静态的Echart信息的部分
              //  var option = generateStaticEchartPart();
                //遍历销量统计列表，动态设定echarts的值
                option.legend.data = data.legendData;
                option.xAxis = data.xAxis;
                option.series = data.series;
                myChart.setOption(option);
            }

        });
        
    }
    /**
     * 生成静态的Echart信息的部分
     */




        /**
         * echarts逻辑部分
         */


        var option = {
            //  提示框，鼠标悬浮交互时的信息提示
            tooltip: {
                trigger: 'axis',
                axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                    type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            //图例，每个图表最多仅有一个图例
            legend: {
                //图例内容数组，数组通常为{String}，每一项代表一个系列的那么、
            },
            //直角坐标系内绘制网格
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            //执教坐标系中横轴数组，数组每一项代表一条横坐标轴
            xAxis: [
                //类目型：需要指定类目列表，坐标轴内有且仅有这些指定目标坐标
                {

                }
            ],
            //直角坐标系中纵轴组，数组中每一项代表一条纵轴坐标轴
            yAxis: [
                {

                }
            ],
            //驱动图表生成的数据内容数组，数组中每一项为一个系类的选项及数据
            series: [
                {
                    name: '波霸奶茶',
                    type: 'bar',
                    data: [120, 132, 101, 134, 290, 230, 220]
                }
            ]
        };







});