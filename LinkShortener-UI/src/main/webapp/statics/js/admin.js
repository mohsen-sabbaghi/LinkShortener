$(function () {
    $('a.remove').css('text-decoration', 'none');
    let linksTable = $('#tbl_links').DataTable();

    $("#tbl_links tr td a.remove").each(function (index) {
        $(this).on("click", function (event) {
            event.preventDefault();
            const elemToRemoveID = $(this).attr('id');
            swal({
                title: "Are you sure?",
                text: "You are going to delete shortLink with ID: " + elemToRemoveID,
                icon: "warning",
                buttons: true,
                dangerMode: true,
            })
                .then((willDelete) => {
                    if (willDelete) {
                        $.ajax({
                            url: '/ls/manage/remove/' + elemToRemoveID,
                            dataType: 'html',
                            head: 'no_content',
                            dataSrc: "",
                            type: 'DELETE',
                            success: function (data) {
                                swal("Poof! Short Link with ID: " + elemToRemoveID + " has been deleted!", {
                                    icon: "success",
                                }).then(function () {
                                    location.reload();
                                });
                            },
                            error: function (data) {
                                swal("Error Calling Server", {
                                    icon: "error",
                                })
                            }
                        });

                    } else {
                        swal("Your link is safe!");
                    }
                });
        });

    });

    $("#tbl_links tr td a.disable").each(function (index) {
        $(this).on("click", function (event) {
            event.preventDefault();
            const elemToRemoveID = $(this).attr('id');
            swal({
                title: "Are you sure?",
                text: "You are going to disable shortLink with ID: " + elemToRemoveID,
                icon: "warning",
                buttons: true,
                dangerMode: true,
            }).then((willDisable) => {
                    if (willDisable) {
                        $.ajax({
                            url: '/ls/manage/disable/' + elemToRemoveID,
                            dataType: 'html',
                            type: 'POST',
                            success: function (data) {
                                swal("Short Link with ID: " + elemToRemoveID + " has been Disabled!", {
                                    icon: "success",
                                }).then(function () {
                                    location.reload(true);
                                });
                            },
                            error: function (data) {
                                swal("Error Calling Server");
                            }
                        });
                    } else {
                        swal("Your link is safe!");
                    }
                });
        });
    });
});
