var saveData;

function filter() {
    saveData = $("#filter").serialize();
    doFilter();
}

function clearFilter() {
    $("#filter").trigger("reset");
    filter();
}

function doFilter() {
    $.ajax({
        type: "GET",
        url: "ajax/admin/meals/filter",
        data: saveData
    }).done(function (data) {
        context.datatableApi.clear().rows.add(data).draw();
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
