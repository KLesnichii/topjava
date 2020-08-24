var saveData;

function filter() {
    saveData = $("#filter").serialize();
    doFilter();
}

function clearFilter() {
    $("#filter").trigger("reset");
    $.get("ajax/admin/meals").done(function (data) {
        reloadTable(data);
    });
}

function doFilter() {
    $.ajax({
        type: "GET",
        url: "ajax/admin/meals/filter",
        data: saveData
    }).done(function (data) {
        reloadTable(data);
    });
}

$(function () {
    makeEditable({
            ajaxUrl: "ajax/admin/meals/",
            datatableApi: $("#datatable").DataTable({
                "paging": false,
                "info": false,
                "searching": false,
                "columns": [
                    {
                        "data": "dateTime"
                    },
                    {
                        "data": "description"
                    },
                    {
                        "data": "calories"
                    },
                    {
                        "defaultContent": "Edit",
                        "orderable": false
                    },
                    {
                        "defaultContent": "Delete",
                        "orderable": false
                    }
                ],
                "order": [
                    [
                        0,
                        "desc"
                    ]
                ]
            })
        }
    );
});
