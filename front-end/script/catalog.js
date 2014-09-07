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
	buildPage(ROMS);
}
function buildPage(roms) {
	// --- 1: by category
	buildCategoryRow("par catégorie", roms, function(rom) {
		var mainCat = rom.cat;
		if(mainCat != null) {
			var idx = mainCat.indexOf('/');
			if(idx > 0)
				mainCat = mainCat.substr(0, idx).trim();
		}
		return mainCat;
	});
	
	// --- 2: by manufacturer
	buildCategoryRow("par constructeur", roms, function(rom) {
		return rom.manuf;
	});
	
	// --- 3: by year
	buildCategoryRow("par année", roms, function(rom) {
		// par décade
		if(rom.year == null)
			return null;
		var tens = Math.floor(rom.year/10);
		return ""+(tens*10)+"-"+((tens+1)*10);
	});
	
	// --- finally focus the first focussable element
	document.getElementsByClassName("cover")[0].focus();
}
function buildCategoryRow(titleText, roms, categoryInterface) {
	// 1: build map
	var cat2Roms = {};
	for(var i=0; i<roms.length; i++) {
		var rom = roms[i];
		var cat = categoryInterface.call(null, rom);
		if(cat == null)
			continue;
		if(cat2Roms[cat] == null) {
			cat2Roms[cat] = [];
		}
		cat2Roms[cat].push(rom);
	}
	// 2: build row
	var items = [];
	for(var cat in cat2Roms) {
		var roms = cat2Roms[cat];
		items.push({type: "category", title: cat+"<br><span class=sub>("+roms.length+")</span>", roms: roms});
	}
	buildRow(titleText+" ("+items.length+")", items);
}

function buildRow(titleText, items) {
	var rowScroller = $("<div>").addClass("cat-rowscroller");
	
	for(var i=0; i<items.length; i++) {
		var item = items[i];
		var rom = items[i].roms[0];
		
		rowScroller
			.append($("<a>")
				.addClass("cover").addClass(item.type)
				.attr('href', '#')
				.on("click", function() {showGamePage(this, item)})
				.on("focus", function() {onFocus(this, item)})
				.on("blur", function() {onBlur(this, item)})
				.append($("<img>")
					.addClass("image")
					.attr("src", "images/mamedb8bits/titles/"+rom.id+".png")
				)
				.append($("<div>")
					.addClass("title")
					.append($("<span>")
						.addClass("label")
						.html(items[i].title)
					)
				)
			);
	}
	
	// add heading and row
	$("#cat-pagescroller")
		.append($("<h1>").text(titleText))
		.append(rowScroller);
}
function onFocus(cover, item) {
	var rowScroller = cover.parentNode;
	var row = rowScroller;
	var rowTitle = row.previousElementSibling;
	var pageScroller = row.parentNode;
	var screen = pageScroller.parentNode;
	
	// force not scrolling
	screen.scrollLeft = 0;
	screen.scrollTop = 0;
//	row.scrollLeft = 0;
//	row.scrollTop = 0;
	
	// TODO: scroll if not fully visible
	var screenHeight = screen.clientHeight;
	var scrollTop = -parseInt(pageScroller.style.top||0);
//	console.log("scroll top: "+scrollTop+"; screen height: "+screenHeight+"; row top: "+rowTitle.offsetTop+"; row bottom: "+(row.offsetTop + row.offsetHeight))
	if(rowTitle.offsetTop - scrollTop < 0) {
		// scroll down
		pageScroller.style.top = ""+(-rowTitle.offsetTop)+"px";
	} else if(row.offsetTop + row.offsetHeight - scrollTop > screenHeight) {
		// scroll up
		pageScroller.style.top = ""+(-(row.offsetTop + row.offsetHeight - screenHeight))+"px";
	}
	
	var screenWidth = screen.clientWidth;
	var scrollLeft = -parseInt(rowScroller.style.left||0);
//	console.log("scroll left: "+scrollLeft+"; screen width: "+screenWidth+"; cover left: "+cover.offsetLeft+"; cover right: "+(cover.offsetLeft + cover.offsetWidth))
	if(cover.offsetLeft - scrollLeft < 20) {
		// scroll right
		rowScroller.style.left = ""+(-(cover.offsetLeft - 20))+"px";
	} else if(cover.offsetLeft + cover.offsetWidth - scrollLeft > screenWidth - 20) {
		// scroll left
		rowScroller.style.left = ""+(-(cover.offsetLeft + cover.offsetWidth - screenWidth + 20))+"px";
	}
	
	// force not scrolling
	screen.scrollLeft = 0;
	screen.scrollTop = 0;
//	row.scrollLeft = 0;
//	row.scrollTop = 0;
	
	// then show snapshot
	var titleImg = cover.getElementsByClassName("image")[0];
	var idx = titleImg.src.lastIndexOf('/');
	titleImg.src = "images/mamedb8bits/snaps"+titleImg.src.substr(idx);
	
	event.preventDefault();
	event.stopPropagation();
	return false;
}
function onBlur(cover, item) {
	var titleImg = cover.getElementsByClassName("image")[0];
	var idx = titleImg.src.lastIndexOf('/');
	titleImg.src = "images/mamedb8bits/titles"+titleImg.src.substr(idx);
	
	event.preventDefault();
	event.stopPropagation();
	return false;
}
function quitPopup() {
	var popup = document.getElementById("popup");
	if(popup != null) {
		popup.parentElement.removeChild(popup);
	}
	CONTROLLER = PAGE_CONTROLLER;
}
function showGamePage(cover, item) {
//	var cover = this;
	
	var rom = item.roms[0];
	
	$(document.body)
		.append($("<div>")
			.attr("id", "popup")
			.append($("<div>")
				.addClass("box")
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
					.addClass("button").addClass("orange")
					.html("play <span class=sub>sf2 on MAME4ALL</span>")
				)
				.append($("<a>")
					.attr("id", "playalt")
					.addClass("button")
					.html("play ...")
				)
			)
		);

	CONTROLLER = POPUP_CONTROLLER;
	
	play.focus();
}
function onKey(e)
{
//	console.log(">>> "+e.keyCode);
	try {
		CONTROLLER(e);
	} catch(e) {
		console.log("error: "+e);
	}
}
function PAGE_CONTROLLER(e) {
//	console.log("active element: "+document.activeElement);
	switch(e.keyCode)
	{
		case 48: // 0
		case 96: // 0
		{
			window.location.reload();
			break;
		}
//		case 13: // ENTER
//		{
//			var x=curSelection.col;
//			var y=curSelection.row;
//			var cell = getCellAt(x, y);
//			if(cell == null) {
//				console.log("empty selection");
//				break;
//			}
//			addClass(cell, "click");
//			e.preventDefault();
//			return false;
//			break;
//		}
		case 37: // LEFT
		{
			var focused = document.activeElement;
			if(focused != null) {
				var next = focused.previousElementSibling;
				if(next)
					next.focus();
			}
			e.preventDefault();
			return false;
			break;
		}
		case 39: // RIGHT
		{
			var focused = document.activeElement;
			if(focused != null) {
				var next = focused.nextElementSibling;
				if(next)
					next.focus();
			}
			e.preventDefault();
			return false;
			break;
		}
		case 38: // UP
		{
			var focused = document.activeElement;
			if(focused != null) {
				var rowScroller = focused.parentNode;
//				var row = rowScroller.parentNode;
				var row = rowScroller;
				var nextRow = row.previousElementSibling.previousElementSibling;
				if(nextRow) {
					row.style.left="13px";
//					var next = nextRow.firstElementChild.firstElementChild;
					var next = nextRow.firstElementChild;
					next.focus();
				}
			}
			e.preventDefault();
			return false;
			break;
		}
		case 40: // DOWN
		{
			var focused = document.activeElement;
			if(focused != null) {
				var rowScroller = focused.parentNode;
//				var row = rowScroller.parentNode;
				var row = rowScroller;
				var nextRow = row.nextElementSibling.nextElementSibling;
				if(nextRow) {
					row.style.left="13px";
//					var next = nextRow.firstElementChild.firstElementChild;
					var next = nextRow.firstElementChild;
					next.focus();
				}
			}
			e.preventDefault();
			return false;
			break;
		}
	}
}
function POPUP_CONTROLLER(e) {
	switch(e.keyCode)
	{
		case 27: // ESC
		{
			quitPopup();
			break;
		}
		case 13: // ENTER
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
var CONTROLLER = PAGE_CONTROLLER;
