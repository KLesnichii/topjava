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
        setCheckbox($(this), id);
    });
});

function setCheckbox(chkbox, id) {
    let enable = chkbox.prop('checked');
    $.ajax({
        url: context.ajaxUrl + id,
        type: "POST",
        data: {enabled: enable}
    }).done(function () {
        $('tr:focus-within').css('color', enable ? 'black' : 'red');
    }).fail(function () {
        $(chkbox).prop("checked", !enable);
    });
}