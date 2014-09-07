/* ===========================================================
 * == desk code
 * =========================================================== */
function onLoad() {
//	var xhr = new XMLHttpRequest();
//	xhr.onreadystatechange = function() {
//	    if (xhr.readyState == 4){
//	    	buildPage(JSON.parse(xhr.responseText));
//	    }
//	};
//	xhr.open('GET', './roms.json', true);
//	xhr.send(null);
	buildGrid(ROMS);
}
var COLUMNS = 5;
var ROWS = 4;
/* TODO: configurable */
var KEYS = {
	ok: 13,
	esc: 27,
	left: 37,
	right: 39,
	up: 38,
	down: 40
}

function buildGrid(roms) {
	var len = Math.min(roms.length, 73);
	var romsPerPage = COLUMNS * ROWS;
	var index = 0;
	while(index < len) {
		$("#grid-scroller").append(buildPage(roms, index));
		index += romsPerPage;
	}
	$("#grid-viewport").on("keydown", onGridKey);

	// --- finally focus the first focussable element
	$(".cover")[0].focus();
}
function buildPage(roms, index) {
	var page = $("<div>").addClass("grid-page");
	var romsPerRow = COLUMNS;
	for(var i=0; i<ROWS; i++) {
		page.append(buildRow(roms, index));
		index += romsPerRow;
		// break if no more cover
		if(index >= roms.length) {
			break;
		}
	}
	return page;
}

function buildRow(roms, index) {
	var row = $("<div>").addClass("grid-row");
	var len = Math.min(roms.length, COLUMNS);
	for(var i=0; i<len; i++) {
		var rom = roms[index+i];
		console.log("rom["+(i+index)+"]: "+rom.id+" ("+rom.name+")")
		
		row.append($("<div>").addClass("grid-cell")
			.append($("<a>")
				.attr("id", "game_"+rom.id)
				.addClass("cover")
				.prop("rom", rom)
				.attr('href', '#')
				.on("click", onClickCover)
				.on("focus", onFocusCover)
				.on("blur", onBlurCover)
				.append($("<img>")
					.addClass("image")
					.attr("src", "images/mamedb8bits/titles/"+rom.id+".png")
					.on("error", onFailCoverImg)
				)
//				.append($("<div>")
//					.addClass("title")
//					.append($("<span>")
//						.addClass("label")
//						.html(items[i].title)
//					)
//				)
			)
		);
	}
	return row;
}
function getGridPosition(cover) {
	if(!cover) {
		var focused = document.activeElement;
		if(focused == null || !$(focused).hasClass("cover")) {
			return null;
		}
		cover = focused;
	}
	var cell = $(cover).parent();
	var row = cell.parent();
	var page = row.parent();
	
	return {
		page: page.index(),
		row: row.index(),
		col: cell.index()
	}
}
function getGridCover(pos) {
//	return $("#grid-scroller .grid-page:nth-child("+(pos.page+1)+") .grid-row:nth-child("+(pos.row+1)+") .grid-cell:nth-child("+(pos.col+1)+") .cover")[0];
	try {
		return document.getElementById("grid-scroller").children[pos.page].children[pos.row].children[pos.col].children[0];
	} catch(e) {
		console.log("error: "+e)
		return null;
	}
}
function scrollToPage(idx) {
	return $("#grid-scroller").css("left", "-"+(idx*1280)+"px")
}
function onFailCoverImg(e) {
	var img = e.target;
	var cover = img.parentElement;
	var rom = cover.rom;
	console.log("failed loading title: "+rom.id);
	$(cover).append($("<div>")
		.addClass("title")
		.text(rom.name+" ("+rom.id+")")
	);
	$(img).remove();
}
function onFocusCover(e) {
	var cover = e.target;
	var rom = cover.rom;
	
//	var row = cover.parentNode;
//	var pageScroller = row.parentNode;
//	var screen = pageScroller.parentNode;
//	
//	// force not scrolling
//	screen.scrollLeft = 0;
//	screen.scrollTop = 0;
//
//	// scroll horizontally
//	var screenWidth = screen.clientWidth;
//	var scrollLeft = -parseInt(pageScroller.style.left||0);
////	console.log("scroll left: "+scrollLeft+"; screen width: "+screenWidth+"; cover left: "+cover.offsetLeft+"; cover right: "+(cover.offsetLeft + cover.offsetWidth))
//	if(cover.offsetLeft - scrollLeft < 0) {
//		// scroll right
////		pageScroller.style.left = ""+(-(cover.offsetLeft))+"px";
//		pageScroller.style.left = ""+(-(scrollLeft - 1280))+"px";
//	} else if(cover.offsetLeft + cover.offsetWidth - scrollLeft > screenWidth - 20) {
//		// scroll left
////		pageScroller.style.left = ""+(-(cover.offsetLeft + cover.offsetWidth - screenWidth + 20))+"px";
//		pageScroller.style.left = ""+(-(scrollLeft + 1280))+"px";
//	}
//	
//	// force not scrolling
//	screen.scrollLeft = 0;
//	screen.scrollTop = 0;
	
	// then show snapshot
	var coverImg = cover.getElementsByClassName("image")[0];
	if(coverImg != null) {
		var idx = coverImg.src.lastIndexOf('/');
		coverImg.src = "images/mamedb8bits/snaps"+coverImg.src.substr(idx);
	}
	event.preventDefault();
	event.stopPropagation();
	return false;
}
function onBlurCover(e) {
	var cover = e.target;
	var rom = cover.rom;
	var coverImg = cover.getElementsByClassName("image")[0];
	if(coverImg != null) {
		var idx = coverImg.src.lastIndexOf('/');
		coverImg.src = "images/mamedb8bits/titles"+coverImg.src.substr(idx);
	}
	event.preventDefault();
	event.stopPropagation();
	return false;
}
function quitDialog(e) {
	$(".dialog-fade").remove();
}
function onClickCover(e) {
	var cover = e.target;
	var rom = cover.rom;
	$(document.body)
		.append($("<div>")
			.addClass("dialog-fade")
			.on("keydown", onDialogKey)
			.append($("<div>")
				.addClass("dialog-box")
				.append($("<div>")
					.addClass("title")
					.html(rom.name)
				)
				.append($("<div>")
					.addClass("category")
					.html(rom.cat)
				)
				.append($("<div>")
					.addClass("year")
					.html(rom.year)
				)
				.append($("<div>")
					.addClass("manufacturer")
					.html(rom.manuf)
				)
				.append($("<img>")
					.addClass("title")
					.attr("src", "images/mamedb8bits/titles/"+rom.id+".png")
				)
				.append($("<img>")
					.addClass("snap")
					.attr("src", "images/mamedb8bits/snaps/"+rom.id+".png")
				)
				.append($("<a>")
					.attr("id", "play")
					.attr("href", "#")
					.addClass("button").addClass("orange")
					.html("play <span class=sub>sf2 on MAME4ALL</span>")
				)
				.append($("<a>")
					.attr("id", "playalt")
					.attr("href", "#")
					.addClass("button")
					.html("play ...")
				)
			)
		);

	$("#play").focus();
}
function onGridKey(e) {
//	console.log("active element: "+document.activeElement);
	switch(e.keyCode)
	{
		case KEYS.left: // LEFT
		{
			var pos = getGridPosition();
			if(pos != null) {
				pos.col--;
				if(pos.col < 0) {
					// scroll page left
					pos.page--;
					pos.col = COLUMNS-1;
					if(pos.page < 0) {
						// no page left
						e.preventDefault();
						return false;
					}
					// scroll page
					scrollToPage(pos.page);
				}
				var next = getGridCover(pos);
				if(next) {
					next.focus();
				}
			}
			e.preventDefault();
			return false;
			break;
		}
		case KEYS.right: // RIGHT
		{
			var pos = getGridPosition();
			if(pos != null) {
				pos.col++;
				if(pos.col >= COLUMNS) {
					pos.page++;
					pos.col = 0;
					// scroll page right
//					if(pos.page == 0) {
					// TODO
//						// no page left
//						e.preventDefault();
//						return false;
//					}
					// scroll page
					scrollToPage(pos.page);
				}
				var next = getGridCover(pos);
				if(next) {
					next.focus();
				}
			}
			e.preventDefault();
			return false;
			break;
		}
		case KEYS.up: // UP
		{
			var pos = getGridPosition();
			if(pos != null) {
				pos.row--;
				if(pos.row < 0) {
					// no page left
					e.preventDefault();
					return false;
				}
				var next = getGridCover(pos);
				if(next) {
					next.focus();
				}
			}
			e.preventDefault();
			return false;
			break;
		}
		case KEYS.down: // DOWN
		{
			var pos = getGridPosition();
			if(pos != null) {
				pos.row++;
				if(pos.row >= ROWS) {
					// no page left
					e.preventDefault();
					return false;
				}
				var next = getGridCover(pos);
				if(next) {
					next.focus();
				}
			}
			e.preventDefault();
			return false;
			break;
		}
	}
}
function onDialogKey(e) {
	switch(e.keyCode)
	{
		case KEYS.esc: // ESC
		{
			quitDialog();
			break;
		}
		case KEYS.ok: // ENTER
		{
//			alert("start game !");
			// Create the event.
//			var event = document.createEvent('Event');
//			event.initEvent('error', true, true);
//			event.message = "coucou";
//			document.dispatchEvent(event);
//			
//			toto.do();
//			backend.runGame("sf2", "mame4all");
			backend.test();
			break;
		}
	}	
}
