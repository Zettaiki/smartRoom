/**
 * 
 * Sets the width of all buttons in a parent element to the width of the longest button.
 */
function EqualizeButtons() {

    var longest = 0;
    var element = $(manual - control - btn - group);

    element.find(".btn").each(function () {
        var width = $(this).width();
        if (longest < width)
            longest = width;
    }).done(function () {
        $('.btn').width(longest);
    });
}

/**
 * 
 * Disable/enable buttons on click.
*/
$("#control-btn").on("click", function () {
    if ($("#scroll-title").hasClass("text-muted")) {
        $("#scroll-title").removeClass("text-muted");
        $("#on-btn").prop("disabled", false);
        $("#on-btn").addClass("border-info");
        $("#off-btn").prop("disabled", false);
        $("#off-btn").addClass("border-info");
        $("#roller-blinds-value").prop("disabled", false);
        $("#control-btn").text("Set automatic");
    } else {
        $("#scroll-title").addClass("text-muted");
        $("#on-btn").prop("disabled", true);
        $("#on-btn").removeClass("border-info");
        $("#off-btn").prop("disabled", true);
        $("#off-btn").removeClass("border-info");
        $("#roller-blinds-value").prop("disabled", true);
        $("#control-btn").text("Take control");
        $("#off-btn").removeClass("active");
        $("#on-btn").removeClass("active");
    }
});

/**
 * Set active class on button click for on and off buttons.
 */
$("#on-btn").on("click", function () {
    $("#on-btn").addClass("active");
    if ($("#off-btn").hasClass("active")) {
        $("#off-btn").toggleClass("active");
    }
});

$("#off-btn").on("click", function () {
    $("#off-btn").addClass("active");
    if ($("#on-btn").hasClass("active")) {
        $("#on-btn").toggleClass("active");
    }
});