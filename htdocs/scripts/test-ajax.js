function init() {
    $.ajax({
        type: 'get',
        url: '/gr3_benkirane/test',
        data: '',
        success: function(ans){alert(ans)},
        error: function(){}
    });
}
