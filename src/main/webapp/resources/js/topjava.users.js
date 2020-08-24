// $(document).ready(function () {

$(function () {
    makeEditable({
            ajaxUrl: "ajax/admin/users/",
            datatableApi: $("#datatable").DataTable({
                "paging": false,
                "info": true,
                "columns": [
                    {
                        "data": "name"
                    },
                    {
                        "data": "email"
                    },
                    {
                        "data": "roles"
                    },
                    {
                        "data": "enabled"
                    },
                    {
                        "data": "registered"
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
                        "asc"
                    ]
                ]
            })
        }
    );
    $(".enabledCheckbox").change(function () {
        let id = $(this).parents("tr").attr("id");
        setCheckbox($(this).prop('checked'), id);
    });
});

function setCheckbox(enabled, id) {
    $.ajax({
        url: context.ajaxUrl + id,
        type: "POST",
        data: {enabled: enabled}
    }).done(function () {
        $('tr:focus-within').css('color', enabled ? 'black' : 'red');
    });
}



