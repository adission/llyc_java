/**
 * Created by jiangyanyan on 2017/8/24.
 */


$(function(){
    showLeft();
    showHeader();

    var $table =$('#table-detail');

    var table = $table.dataTable($.extend(true,{},CONSTANT.DATA_TABLES.DEFAULT_OPTION,{
        "ajax":{
            url: 'data/good.json',
            type: 'POST',
            data: {
                //"paramJson":JSON.stringify(paramsData)
            },
        },
        columns: [
            CONSTANT.DATA_TABLES.COLUMN.CHECKBOX,
            { "data": "name"},
            { "data": "num" },
            { "data": "startTime" },
            { "data": "managePerson" },
            { "data": null },
            { "data": "lowest" },
            { "data": "" }
        ],
        "columnDefs": [
            {
                "orderable": true,
                "render": function (data, type, full) {
                    return '<div>' + data + '</div>';
                },
                "targets": 1
            },
            {
                "orderable": false,
                "render": function (data, type, full) {
                    return '<div >' + data + '</div>';
                },
                "targets": 2
            },
            {
                "orderable": false,
                "render": function (data, type, full) {
                    return '<div>' + data + '</div>';
                },
                "targets": 3
            },
            {
                "orderable": false,
                "render": function (data, type, full) {
                    return '<div>' + data + '</div>';
                },
                "targets": 4
            },
            {
                "orderable": false,
                "render": function (data, type, full) {
                    return "<button class='btn btn-primary' onclick='attendanceList("+full.user_name+")'>查看</button>";//课程时间
                },
                "targets": 5
            },
            {
                "orderable": false,
                "render": function (data, type, full) {
                    return '<div>' + data + '</div>';
                },
                "targets": 6
            },
            {
                "orderable": false,
                "render": function (data, type, full) {
                    return ' <div class="btn-group"> ' +
                        '<button type="button" class="btn btn-default" title="编辑" onclick="updateUser()"><i class="glyphicon glyphicon-file text-primary"></i></button> ' +
                        '<button type="button" class="btn btn-default" title="删除"><i class="glyphicon glyphicon-remove text-danger"></i></button>' +
                        ' </div>	';
                },
                "targets": 7
            },
        ],
        "oLanguage": {
            "sUrl": "http://cdn.datatables.net/plug-ins/1.10.12/i18n/Chinese.json"
        },

    })).api();

    //checkbox全选
    $("#checkAll").on("click", function () {
        if ($(this).prop("checked") === true) {
            $("input[name='checkList']").prop("checked", $(this).prop("checked"));
            $('#table-detail tbody tr').addClass('selected');
        } else {
            $("input[name='checkList']").prop("checked", false);
            $('#table-detail tbody tr').removeClass('selected');
        }
    });
});
// 点击编辑按钮
function updateUser(){
    $('#myModal1').modal("show");
}
// 点击查看按钮
function attendanceList(item){
    var $table1 =$('#user');
    table1 = $table1.dataTable($.extend(true,{}, CONSTANT.DATA_TABLES.DEFAULT_OPTION,{
        "ajax":{
            url: 'data/us.json',
            type: 'POST',
            data: {
                //"paramJson":JSON.stringify(paramsData)
            },
        },
        columns: [
            { "data": "user_name" },
            { "data": "age" },
            { "data": "mobile" },
            { "data": "cid" },
            { "data": "workers_type" },
        ],
        "columnDefs": [
            {
                "clasName": "text-center",
                "orderable": true,
                "render": function (data, type, full) {
                    //console.log(data);
                    return '<div>' + data + '</div>';
                },
                "targets": 0
            },
            {
                "orderable": true,
                "render": function (data, type, full) {
                    return '<div>' + data + '</div>';
                },
                "targets": 1
            },
            {
                "orderable": false,
                "render": function (data, type, full) {
                    return '<div >' + data + '</div>';
                },
                "targets": 2
            },
            {
                "orderable": false,
                "render": function (data, type, full) {
                    return '<div>' + data + '</div>';
                },
                "targets": 3
            },
            {
                "orderable": false,
                "render": function (data, type, full) {
                    return '<div>' + data + '</div>';
                },
                "targets": 4
            },

        ],
        "oLanguage": {
            "sUrl": "http://cdn.datatables.net/plug-ins/1.10.12/i18n/Chinese.json"
        },
    })).api();
    $('#myModal2').modal("show");

}



