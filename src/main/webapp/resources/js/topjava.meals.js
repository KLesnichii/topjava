var mealAjaxUrl = "ajax/profile/meals/";

function updateFilteredTable() {
    $.ajax({
        type: "GET",
        url: mealAjaxUrl + "filter",
        data: $("#filter").serialize()
    }).done(updateTableByData);
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get(mealAjaxUrl, updateTableByData);
}

$(function () {
    makeEditable({
        ajaxUrl: mealAjaxUrl,
        datatableApi: $("#datatable").DataTable({
            "ajax": {
                "url": mealAjaxUrl,
                "dataSrc": ""
            },
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime",
                    "render": function (date, type, row) {
                        if (type === "display") {
                            return date.substring(0, 10) + " " + date.substring(11, 16);
                        }
                        return date;
                    }
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false,
                    "render": renderEditBtn
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false,
                    "render": renderDeleteBtn
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ],
            "createdRow": function (row, data, dataIndex) {
                if (data.excess) {
                    $(row).attr("data-mealExcess", true);
                } else {
                    $(row).attr("data-mealExcess", false);
                }
            }
        }),
        updateTable: updateFilteredTable
    });
});

$.ajaxSetup({
    converters: {
        "text json": function (result) {
            result = JSON.parse(result);
            for (let i in result) {
                if (i === "dateTime") {
                    result[i] = result[i].substring(0, 10) + " " + result[i].substring(11, 16);
                }
            }
            return result;
        }
    }
});

$("#dateTime").datetimepicker({
    format: 'Y-m-d H:00'
});
$("#startTime").datetimepicker({
    datepicker: false,
    format: 'H:00'
});
$("#endTime").datetimepicker({
    datepicker: false,
    format: 'H:00'
});
$("#startDate").datetimepicker({
    timepicker: false,
    format: 'Y-m-d'
});
$("#endDate").datetimepicker({
    timepicker: false,
    format: 'Y-m-d'
});