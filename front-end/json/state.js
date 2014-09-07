STATE = {
	filters: [
	   {
		   id: "category",
		   title: "Catégorie",
		   values: [
		       {id: "Shooter"},
		       {id: "Platform"},
		       {id: "Fighter"},
		       {id: "Breakout"},
		       {id: "Puzzle"},
		       {id: "Driving"},
		       {id: "Maze"},
		       {id: "Climbing"},
		       {id: "Wrestling"},
		       {id: "Sports"}
		   ]
	   },
	   {
		   id: "emulator",
		   title: "Emulateur",
		   values: [
		       {id: "mame4all", title: "MAME4All"},
		       {id: "fba", title: "Final Burn Alpha"},
		       {id: "gngeo", title: "GnGeo"},
		       {id: "advmame", title: "Advance MAME"}
		   ]
	   },
	   {
		   id: "year",
		   title: "Année",
		   values: [
		       {id: "1970s", title: "1970-1979"},
		       {id: "1980s", title: "1980-1989"},
		       {id: "1990s", title: "1990-1999"},
		       {id: "2000s", title: "2000-2009"},
		       {id: "2010s", title: "2010-2019"}
		   ]
	   },
	   {
		   id: "working",
		   title: "Fonctionnel",
		   values: [
		       {id: "ok", title: "Oui"},
		       {id: "ko", title: "Non"}
		   ]
	   }
	],
	sort: [
       {
    	   id: "name",
    	   title: "nom"
       },
       {
    	   id: "lastPlayed",
    	   title: "date joué"
       },
       {
    	   id: "timesPlayed",
    	   title: "nombre de parties"
       }
	],
	lastState: {
		filters: {
			"emulator": ["mame4all", "fba", "gngeo"],
			"year": ["2000s", "2010s"],
			"working": ["ok"]
		}, 
		sort: {
			by: "name",
			ascending: true
		},
		page: 4,
		game: "sf2";
	}
}