/*:
 * @title Procedural Text
 * @plugindesc This plugin generates procedural text for a Romeo & Juliet discussion.
 *
 *
 * @author TitusTimesTen
 *
 */

var Imported = Imported || {};
Imported.ProceduralText = true;

var ProcText = ProcText || {};
ProcText.Plugin = ProcText.Plugin || {};


(function(){

	ProcText.Plugin.evalbeat1 = function(){
		var count = 0
		if ($gameSwitches.value(1) === true) { count++ }
		if ($gameSwitches.value(5) === true) { count++ }
		if ($gameSwitches.value(7) === true) { count++ }
		return count >= 2
	}

	ProcText.Plugin.evalbeat3 = function(){
		var count = 0
		if ($gameSwitches.value(10) === true) { count++ }
		if ($gameSwitches.value(12) === true) { count++ }
		if ($gameSwitches.value(15) === true) { count++ }
		return count >= 2
	}

	ProcText.Plugin.evalbeat4 = function(){
		var count = 0
		if ($gameSwitches.value(2) === true) { count++ }
		if ($gameSwitches.value(9) === true) { count++ }
		if ($gameSwitches.value(13) === true) { count++ }
		if ($gameSwitches.value(14) === true) { count++ }
		return count >= 3
	}

	ProcText.Plugin.setTeacher = function(){
		var v = $gameVariables.value(10)
		switch (v) {
			case 0:
				$gameMessage.setFaceImage('M1', 0)
				break
			case 1:
				$gameMessage.setFaceImage('M2', 0)
				break
			case 2:
				$gameMessage.setFaceImage('M3', 0)
				break
			case 3:
				$gameMessage.setFaceImage('M4', 0)
				break
			case 4:
				$gameMessage.setFaceImage('F1', 0)
				break
			case 5:
				$gameMessage.setFaceImage('F2', 0)
				break
			case 6:
				$gameMessage.setFaceImage('F3', 0)
				break
			case 7:
				$gameMessage.setFaceImage('F4', 0)
		}
	}

	var beats = []

	DiscussionTracker = function() {

		this.characters = [],
		this.MQs = [],
		this.TQs = [],
		this.discs = {},
		//codes: 0 is not covered, 1 is recently triggered, 2 is triggered, 3 is covered
		this.discCovered = {},
		this.bitsCovered = [[], [], [], []],
		this.beatsCovered = [],
		this.beat = [],
		this.beat_index1 = 0,
		this.beat_index2 = 0,
		this.question = [],
		this.speaker = 'T',
		this.topic = '',
		this.subject = ''
		this.omit = ''
		this.queue = []
		this.conversion = []
		this.disc = {}
		this.dLet = null
		this.confirmations = ['Right. ', 'Good. ', 'Right on. ', 'Uh huh. ', 'Yes. ', 'Yep. ', 'Very good. ', 'That\'s right. ', 'Yes, great answer. ', 'You got it right. ', 'Good job. ', 'You\'re correct. ']
	}

	DiscussionTracker.prototype.init = function (beat, charac, questMaj, questTr){
		this.beat_index1 = 1
		var l = []
		var x
		l = []
		for (x in [...Array(19)] ){
			l.push(false)
		}
		this.bitsCovered[0].push(l)
		l = []
		for (x in [...Array(9)] ){
			l.push(false)
		}
		this.bitsCovered[0].push(l)
		l = []
		for (x in [...Array(10)] ){
			l.push(false)
		}
		this.bitsCovered[0].push(l)

		l = []
		for (x in [...Array(12)] ){
			l.push(false)
		}
		this.bitsCovered[1].push(l)
		l = []
		for (x in [...Array(13)] ){
			l.push(false)
		}
		this.bitsCovered[1].push(l)
		l = []
		for (x in [...Array(20)] ){
			l.push(false)
		}
		this.bitsCovered[1].push(l)
		l = []
		for (x in [...Array(11)] ){
			l.push(false)
		}
		this.bitsCovered[1].push(l)
		l = []
		for (x in [...Array(20)] ){
			l.push(false)
		}
		this.bitsCovered[1].push(l)
		for (x in [...Array(questMaj.length)] ){
			this.beatsCovered.push(false)
		}

		l = []
		for (x in [...Array(14)] ){
			l.push(false)
		}
		this.bitsCovered[2].push(l)

		l = []
		for (x in [...Array(18)] ){
			l.push(false)
		}
		this.bitsCovered[3].push(l)
		l = []
		for (x in [...Array(21)] ){
			l.push(false)
		}
		this.bitsCovered[3].push(l)
		l = []
		for (x in [...Array(11)] ){
			l.push(false)
		}
		this.bitsCovered[3].push(l)

		this.changeBeat(beat)
		this.characters = charac
		this.MQs = questMaj
		this.TQs = questTr
		this.discCovered = {'A': 0, 'B': 0, 'E': 0, 'F': 0, 'G': 0, 'H': 0, 'I': 0, 'J': 0, 'L': 0, 'M': 0, 'N': 0, 'O': 0}
		return
	}

	DiscussionTracker.prototype.changePart = function (partNum) {
		beats = []
		switch (partNum) {
			case 1:
				this.part1()
				break;
			case 2:
				this.part2()
				break;
			case 3:
				this.part3()
				break;
			case 4:
				this.part4()
				break;
		}
		this.beat_index1 = partNum
	}

	DiscussionTracker.prototype.part1 = function() {
		beats.push($p1b1)
		beats.push($p1b2)
		beats.push($p1b3)
	}

	DiscussionTracker.prototype.part2 = function() {
		beats.push($p2b1)
	    beats.push($p2b2)
	    beats.push($p2b3)
	    beats.push($p2b4)
	    beats.push($p2b5)
	}

	DiscussionTracker.prototype.part3 = function() {
		beats.push($p3b1)
	}

	DiscussionTracker.prototype.part4 = function() {
		beats.push($p4b1)
		beats.push($p4b2)
		beats.push($p4b3)
	}

	DiscussionTracker.prototype.changeBeat = function (beat){
		this.beat = beat
		if (this.beat != null) {
			this.beat_index1 = beat['part'] - 1
			this.beat_index2 = beat['beat'] - 1
		} else {
			this.beat_index2 = null
		}
		return
	}

	DiscussionTracker.prototype.askMajorQ = function() {
		this.beatsCovered[this.beat['beat'] - 1] = true

		var qnum = Math.floor( Math.random()*this.beat['majorQ'][0]['answers'].length )
		if (this.beat['majorQ'][0]['answers'][qnum]['gen']){
			this.askProcGenQ(this.beat['majorQ'][0]['answers'][qnum])
		} else {
			//queue teacher's question
			this.speaker = 'T'
			this.queueLine(this.speaker, this.parse(this.beat['majorQ'][0]['text'][0]))

			//queue students' response
			for (i in this.beat['majorQ'][0]['answers'][qnum]['text'][0]){
				var line = this.parse(this.beat['majorQ'][0]['answers'][qnum]['text'][0][i])
				this.queueLine(this.speaker, line)
			}

			//queue teacher response
			if(!!this.beat['majorQ'][0]['response']) {
				var conf = this.confirmations[Math.floor(Math.random()*this.confirmations.length)]
				this.queueLine('T', this.parse(conf + this.beat['majorQ'][0]['response']) )
			}

			//markoff bitsCovered material and check for triggers
			for (i in this.beat['majorQ'][0]['answers'][qnum]['data'][0]) {
				var v = this.beat['bits'][(   this.beat['majorQ'][0]['answers'][qnum]['data'][0][i]   % 100) - 1]['trigger'][0]
				if (v !== undefined && v !== null) {
					this.discCovered[this.beat['bits'][(   this.beat['majorQ'][0]['answers'][qnum]['data'][0][i]   % 100) - 1]['trigger'][0]] = 1
				}
				this.bitsCovered[this.beat_index1][this.beat_index2][(i % 100) - 1] = true
			}
		}
		return
	}

	DiscussionTracker.prototype.askProcGenQ = function(quest) {
		this.question = quest

		//queue teacher's question
		//------------------------
		this.speaker = 'T'
		this.queueLine(this.speaker, this.parse(this.question['text'][0]))
		this.topic = this.question['topic'][0]

		//queue students' response
		//------------------------
		//choose answer path
		var path = Math.floor(Math.random()*this.question['answer'].length)

		//find all possible combinations
		//---includes all singular answer entries 
		//---as well as all pairs of answer and neighborhood entries
		var combos = []
		for (i in this.question['answer'][path]) {
			combos.push([this.question['answer'][path][i]])
			for (j in this.question['neighborhood'][0]) {
				if (this.question['answer'][path][i] !== this.question['neighborhood'][0][j]) {
					combos.push([this.question['answer'][path][i], this.question['neighborhood'][0][j] ])
				}
			}
		}

		//---validate
		//---removes combos with incorrect syntax
		//---check each combo to see if valid
		var valid = []
		for (i in [...Array(combos.length)] ) {
			valid.push(true)
		}
		var index = 0
		for (i in combos) {
			if (combos[i].length === 2) {
				var types = []
				var b_vals = []
				for (j in combos[i]) {
					types.push([combos[i][j] % 100, this.beat['bits'][(combos[i][j] % 100) - 1]['types'][0] ])
					var b4 = this.beat['bits'][(combos[i][j] % 100) - 1]['b4'][0]
					var b5 = this.beat['bits'][(combos[i][j] % 100) - 1]['b5'][0]
					var b6 = this.beat['bits'][(combos[i][j] % 100) - 1]['b6'][0]
					b_vals.push([b4, b5, b6])
				}
				valid[index] = this.validate(types, b_vals)
			}
			index += 1
		}
		//--pop off invalid combos
		var count = 0
		for (i in [...Array(valid.length)] ) {
			if (valid[i] === false) {
				combos.splice(i - count, 1)
				count += 1
			}
		}

		//choose one that has no repeated bits
		var sets = []
		var indicesB = [ [null], [null], [null], [null], [null] ]			//<--5 parameter limit for question['answer'][path]
		for (i in this.question['answer'][path]) {
			indicesB[i] = []
			var count = 0
			for (j in combos) {
				if (combos[j].includes(this.question['answer'][path][i])) {
					indicesB[i].push(combos[j])
				}
			}
		}
		for (i in indicesB[0]) {
			for (j in indicesB[1]) {
				for (k in indicesB[2]) {
					for (l in indicesB[3]) {
						for (m in indicesB[4]) {
							sets.push( [ indicesB[0][i], indicesB[1][j], indicesB[2][k], indicesB[3][l], indicesB[4][m] ] )
						}
					}
				}
			}
		}

		var valid = false
		var choice
		while (!valid) {
			valid = true
			choice = Math.floor(Math.random()*sets.length)
			for (i in sets[choice]) {
				for (j in sets[choice]) {
					if (i !== j && !!sets[choice][i] && sets[choice][j] ) {
						var identicalArrays = true
						Loop: for (x in sets[choice][i]) {
							for (y in sets[choice][j]) {
								if (x === y && sets[choice][i][x] !== sets[choice][j][y]) {
									identicalArrays = false;
									break Loop;
								}
							}
						}
						if (identicalArrays) {
							sets[choice][j] = null
						} else if (!!sets[choice][i] && !!sets[choice][j] && this.invalidate2([ sets[choice][i], sets[choice][j]]) ) {
							valid = false
						}
					}
				}
			}
			if (!valid) {
				sets.splice(choice, 1)
			}
		}

		//---now call another method to show response
		var responses = []
		for (i in sets[choice]) {
			if (!!sets[choice][i])
				responses.push(sets[choice][i])
		}
		this.showResponses(responses)

		//queue teacher response
		if(!!this.question['response']) {
			var conf = this.confirmations[Math.floor(Math.random()*this.confirmations.length)]
			this.queueLine('T', this.parse(conf + this.question['response']) )
		}

		//markoff bitsCovered material and check for triggers
		//------------------------
		for (i in responses) {
			for (j in responses[i]) {
				if (this.beat['bits'][(responses[i][j] % 100) - 1]['trigger'][0] != null && this.discCovered[this.beat['bits'][(responses[i][j] % 100) - 1]['trigger'][0]] !== 3) {
					this.discCovered[this.beat['bits'][(responses[i][j] % 100) - 1]['trigger'][0]] = 1
				}

				this.bitsCovered[this.beat_index1][this.beat_index2][(responses[i][j] % 100) - 1] = true
			}
		}
		return
	}

	DiscussionTracker.prototype.getMinorQ = function(){
		//return a list of minor questions to be chosen from
		questions = []
		for (i in [...Array(this.beat['minorQ'].length)] ){
			questions.push(this.beat['minorQ'][i])
		}
		return questions
	}

	DiscussionTracker.prototype.validate = function (types, b_vals) {
		//types is an array of the form:
		//	[[A_index, [A_type1],...[A_typeN] ], [B_index, [B_type1],...[B_typeN] ]]
		//	where A and B are not identical
		//b_vals is an array of the form
		//  [[A_b4, A_b5, A_b6], [B_b4, B_b5, B_b6] ]
		var data = {'A': types[0], 'B': types[1]}

		var result1 = this.__val__(data['A'], data['B'], b_vals[0], b_vals[1])
		var result2 = this.__val__(data['B'], data['A'], b_vals[1], b_vals[0])

		if (result1 && result2) {
			return true
		} else {
			return false
		}

		return
	},

	DiscussionTracker.prototype.invalidate2 = function (data) {
		var invalid = false
		for (let i in [...Array(data.length)] ) {
			for (let x in [...Array(data.length)] ) {
				for (let j in data[i]) {
					for (let y in data[x]) {
						if (!!data[x][y] && !!data[i][j] && data[x][y] === data[i][j] && i !== x) {
							invalid = true
						}
					}
				}
			}
		}
		return invalid
	}

	DiscussionTracker.prototype.__val__ = function (arg1, arg2, arg1b, arg2b) {
		//arg1: [ [index, [type1],...[typeN] ]		arg1b:	[b4, b5, b6]
		//arg2: [ [index, [type1],...[typeN] ]		arg2b:	[b4, b5, b6]

		//Assume things are valid until proven otherwise
		var validity = []
		for (x in arg1[1]) {
			validity.push(true)
		}

		var orderA = this.beat['bits'][arg1[0] - 1]['b3'][0]
		var orderB = this.beat['bits'][arg2[0] - 1]['b3'][0]

		if (orderA === orderB) {
			//bits have same b3 timestamp, i.e. 0 away
			
			for (x in arg1[1]) {
				function fjs (a) { return a === arg1[1][x] }
				//now for this element, assume false until proven true
				validity[arg1[1].findIndex(fjs)] = false

				if (arg1[x] === 'B6_1') {
					var ref = this.beat['bits'][arg2[0] - 1]['b6'][0]
					if (!(ref === null || ref % 100 !== arg1[0]) ) {
						validity[arg1[1].findIndex(fjs)] = true
					}
				}

				else {
					for (y in arg2[1] )
						if (eval0(arg1[1][x], arg2[1][y], arg1b, arg2b, arg1[0], arg2[0]) ) {
							validity[arg1[1].findIndex(fjs)] = true
						}
				}
			}
		}

		else if (orderA === orderB + 1 || orderA === orderB - 1) {
			//bits are 1 away

			for (x in arg1[1]) {
				function fjs (a) { return a === arg1[1][x] }

				if (x === 'B6_1') {
					validity[arg1[1].findIndex(fjs)] = false
				}

				else {
					//now for this element, assume false until proven true
					validity[arg1[1].findIndex(fjs)] = false
					for (y in arg2[1]) {
						if (eval1(arg1[1][x], arg2[1][y])) {
							validity[arg1[1].findIndex(fjs)] = true
						}
					}
				}
			}
		}

		else {
			//bits are more than 1 away
			for (x in arg1[1]) {
				function fjs (a) { return a === arg1[1][x] }
				validity[arg1[1].findIndex(fjs)] = false
			}
		}

		var valid = true
		for (i in validity) {
			if (validity[i] === false) {
				valid = false
			}
		}

		return valid
	},

	DiscussionTracker.prototype.showResponses = function(responses) {
		for (i in responses) {
			//set speaker randomly
			if (this.speaker === 'K') {
				this.speaker = 'H'
			} else if (this.speaker === 'H') {
				this.speaker = 'K'
			} else {
				var s = Math.floor(Math.random()*2)
				if (s === 0){
					this.speaker = 'K'
				} else {
					this.speaker = 'H'
				}
			}

			if (responses[i].length === 1) {
				var p1 = this.parse(this.beat['bits'][(responses[i][0]%100)-1]['text'][0], this.beat['bits'][(responses[i][0]%100)-1]['b1'][0], this.beat['bits'][(responses[i][0]%100)-1]['b2'][0], false).split(' ')
				var p2 = ''
				for (j = 1; j < p1.length; j++){
					p2 += ' ' + p1[j]
				}
				var pstring = '[' + this.speaker + '] ^' + p1[0] + '^' + p2 + '.'

				var mytext = this.parse(pstring, this.beat['bits'][(responses[i][0]%100)-1]['b1'][0], this.beat['bits'][(responses[i][0]%100)-1]['b2'][0]) 
				
				this.queueLine(this.speaker, mytext)
			} else if (responses[i].length === 2) {
				var theseBits = arrange(this.beat['bits'][(responses[i][0]%100)-1], this.beat['bits'][(responses[i][1]%100)-1])
				var line = this.parse2(theseBits)
				this.queueLine(this.speaker, line)
			}
		}
		return
	}

	DiscussionTracker.prototype.parse = function(string, sub = null, ob = null, lineBreak = true, replaceSub = false) {
		var s = string
		if ( s.includes('[K]') ) {
			this.speaker = 'K'
			s = s.replace("[K]", "KaYeon:")
			$gameMessage.setFaceImage("KaYeon", 0)
		}
		if ( s.includes("[H]") ) {
			this.speaker = 'H'
			s = s.replace("[H]", "HwiYoung:")
			$gameMessage.setFaceImage("HwiYoung", 0)
		}
		if (this.topic !== '') {
			if (!!sub && this.topic === sub) {
				s = s.replace(this.topic, this.characters[this.topic]['pronoun'][0])
			}
			if (!!ob && this.topic === ob) {
				s = s.replace(this.topic, this.characters[this.topic]['pronoun'][1])
			}
		}
		if (replaceSub && !!sub && this.subject !== '') {
			if (this.subject === sub) {
				s = s.replace(this.subject, this.characters[this.subject]['pronoun'][0])
			}
		}
		if (replaceSub && !!ob && this.subject !== '') {
			if (this.subject === ob) {
				s = s.replace(this.subject, this.characters[this.subject]['pronoun'][1])
			}
		}

		if ( s.includes('^') ) {
			var strings = s.split('^')
			s = ''

			for (var strn in strings){
				if (strn % 2 === 1) {
					strings[strn] = strings[strn].charAt(0).toUpperCase() + strings[strn].slice(1)
				}
				s += strings[strn]
			}
		}

		if (lineBreak) {
			var _index = 43
			var _split = s.charAt(_index)
			while (!!_split) {
				while (s[_index] !== ' ') {
					_index--
				}
				s = s.substring(0, _index) + '\n' + s.substring(_index + 1, s.length)

				_index += 43
				_split = s.charAt(_index)
			}
		}

		return s
	},

	DiscussionTracker.prototype.parse2 = function(bits) {
		//assumes bits are already in the proper order
		
		var flip = false
		var conjunction
		var cap = false

		if (bits['bit1']['types'][0].includes('A0') && (bits['bit2']['types'][0].includes('A0')
		|| bits['bit2']['types'][0].includes('A0.') || bits['bit2']['types'][0].includes('A0-')
		|| bits['bit2']['types'][0].includes('A0+') )) {
			conjunction = ', so '
		}
		else if (bits['bit1']['types'][0].includes('A0.') && (bits['bit2']['types'][0].includes('A0')
		|| bits['bit2']['types'][0].includes('A0.') || bits['bit2']['types'][0].includes('A0-')
		|| bits['bit2']['types'][0].includes('A0+') )) {
			conjunction = '. Then '
		}
		else if (bits['bit1']['types'][0].includes('A0-') && (bits['bit2']['types'][0].includes('A0')
		|| bits['bit2']['types'][0].includes('A0.') || bits['bit2']['types'][0].includes('A0-')
		|| bits['bit2']['types'][0].includes('A0+') )) {
			conjunction = ', and '
		}
		else if (bits['bit1']['types'][0].includes('A0+') && (bits['bit2']['types'][0].includes('A0')
		|| bits['bit2']['types'][0].includes('A0.') || bits['bit2']['types'][0].includes('A0-')
		|| bits['bit2']['types'][0].includes('A0+') )) {
			conjunction = ' when '
			flip = true
		}
		else if (bits['bit1']['types'][0].includes('A1') && (bits['bit2']['types'][0].includes('A0')
		|| bits['bit2']['types'][0].includes('A0.') || bits['bit2']['types'][0].includes('A0-')
		|| bits['bit2']['types'][0].includes('A0+') )) {
			conjunction = ' when '
		}
		else if (bits['bit2']['types'][0].includes('A3')) {
			conjunction = ', so '
		}
		else if (bits['bit1']['types'][0].includes('A2') && bits['bit2']['types'][0].includes('A1')) {
			conjunction = ', but '
		}
		else if (bits['bit1']['types'][0].includes('A3') && bits['bit2']['types'][0].includes('A1')) {
			conjunction = ', because '
		}
		else if (bits['bit1']['types'][0].includes('A5') && bits['bit2']['types'][0].includes('B6_1')) {
			conjunction = '. Maybe it\'s because '
		}
		else if (bits['bit2']['types'][0].includes('A9')) {
			conjunction = '. '
			cap = true
		}
		else if (bits['bit1']['types'][0].includes('A9')) {
			conjunction = '. '
			cap = true
			if (! ('B6_1' in bits['bit2']['types'][0])) {
				flip = true
			}
		}
		else {
			//default conjunction
			conjunction = ', and '
		}

		var piece1
		var piece2
		var sub = []
		var ob = []
		if (flip) {
			var num = Math.floor(Math.random()*bits['bit2']['text'].length)
			sub = [ bits['bit2']['b1'][0], bits['bit1']['b1'][0] ]
			ob = [ bits['bit2']['b2'][0], bits['bit1']['b2'][0] ]
			this.subject = sub[0]

			piece1 = bits['bit2']['text'][num]
			piece1 = this.parse(piece1, sub[0], ob[0], false)
			num = Math.floor(Math.random()*bits['bit1']['text'].length)
			piece2 = bits['bit1']['text'][num]
			piece2 = this.parse(piece2, sub[1], ob[1], false, true)
		} else {
			var num = Math.floor(Math.random()*bits['bit1']['text'].length)
			sub = [ bits['bit1']['b1'][0], bits['bit2']['b1'][0] ]
			ob = [ bits['bit1']['b2'][0], bits['bit2']['b2'][0] ]
			this.subject = sub[0]

			piece1 = bits['bit1']['text'][num]
			piece1 = this.parse(piece1, sub[0], ob[0], false)
			num = Math.floor(Math.random()*bits['bit2']['text'].length)
			piece2 = bits['bit2']['text'][num]
			piece2 = this.parse(piece2, sub[1], ob[1], false, true)
		}
		this.subject = ''

		//This setting replaces pronouns when they match the subject of the teacher's question.
		/*
		if (sub[0] === sub[1] && sub[0] !== null && sub[1] !== undefined) {
			if (this.topic !== sub[0] && this.topic !== sub[1] && this.topic !== ob[0] && this.topic !== ob[1]) {
				piece2 = piece2.replace(sub[0], this.characters[sub[0]]['pronoun'][0])
			}
		}*/

		var parsed = ''
		if (!cap) {
			parsed = piece1 + conjunction + piece2 + '.'
		} else {
			var p1 = piece2.split(' ')
			var p2 = ''
			for (i = 1; i < p1.length; i++) {
				p2 += ' ' + p1[i] 
			}
			parsed = piece1 + conjunction + '^' + p1[0] + '^' + p2 + '.'
		}

		var p1 = parsed.split(' ')
		var p2 = ''
		for (i = 1; i < p1.length; i++) {
			p2 += ' ' + p1[i] 
		}
		if (this.speaker === 'H') {
			parsed = '[H] ^' + p1[0] + '^' + p2
		} else {
			parsed = '[K] ^' + p1[0] + '^' + p2
		}

		parsed = this.parse(parsed)

		return parsed
	}

	DiscussionTracker.prototype.queueLine = function(speaker, line) {
		this.queue.push({'speaker': speaker, 'line': line})
	}

	DiscussionTracker.prototype.nextLine = function() {
		if (!!this.queue[0]) {
			var line = this.queue[0]
			this.queue.shift()

			$gameMessage.newPage()

			switch (line.speaker) {
				case 'T':
					ProcText.Plugin.setTeacher()
					break
				case 'H':
					$gameMessage.setFaceImage('HwiYoung', 0)
					break
				case 'K':
					$gameMessage.setFaceImage('KaYeon', 0)
					break
				case '[T]':
					ProcText.Plugin.setTeacher()
					break
				case '[H]':
					$gameMessage.setFaceImage('HwiYoung', 0)
					break
				case '[K]':
					$gameMessage.setFaceImage('KaYeon', 0)
					break
			}

			$gameMessage.add(line.line)
			$gameMap._interpreter.setWaitMode('message')
		}
		else {
			$gameMap._interpreter.setWaitMode('')
		}

		return !!this.queue[0]
	}

	eval0 = function(t1, t2, t1b, t2b, t1index, t2index) {
		//checks whether t1 is compatible with t2 at distance 0
		//t1, t2 are types of bits, passed as strings
		//t1b, t2b are database values [b4, b5, b6]
		if (t1 === 'A0' || t1 === 'A0-' || t1 === 'A0.' || t1 === 'A0+') {
			if (t2 === 'A0' || t2 === 'A0-' || t2 === 'A0.' || t2 === 'A0+') {
				return false
			} else {
				return true
			}
		} else if (t1 === 'A1') {
			if (t2 === 'A0' || t2 === 'A0-' || t2 === 'A0.' || t2 === 'A0+') {
				return true
			} else if (t2 === 'A2' || t2 === 'A3' || t2 === 'A4') {
				if (t2b[1] === t1index) {
					return true
				} else {
					return false
				}
			} else {
				return false
			}
		} else if (t1 === 'A2' || t1 === 'A3' || t1 === 'A4') {
			if (t2 === 'A0' || t2 === 'A0-' || t2 === 'A0.' || t2 === 'A0+' || t2 === 'A1') {
				return true
			} else {
				return false
			}
		} else if (t1 === 'A5') {
			if (t2 === 'A0' || t2 === 'A0-' || t2 === 'A0.' || t2 === 'A0+' || t2 === 'B6_1') {
				return true
			} else {
				return false
			}
		}
		else if (t1 === 'A6' || t1 === 'A7' || t1 === 'A8' || t1 === 'A9') {
			return true
		} else {
			return false
		}
		return false
	}

	eval1 = function(t1, t2) {
		//checks whether t1 is compatible with t2 at distance 1
		//t1, t2 are types of bits, passed as strings
		if (t1 === 'A0' || t1 === 'A0-' || t1 === 'A0.' || t1 === 'A0+') {
			if (t2 === 'A0' || t2 === 'A0-' || t2 === 'A0.' || t2 === 'A0+') {
				return true
			}
		}
		return false
	}

	arrange = function(b1, b2) {
		var a = {}
		var flip = false

		if (b1['b3'][0] < b2['b3'][0]) {
			flip = false
		} else if (b1['b3'][0] > b2['b3'][0]) {
			flip = true
		} else {
			if ( (b2['types'][0].includes('A2') || b2['types'][0].includes('A3')) && b1['types'][0].includes('A1')) {
				flip = true
			} else if (b2['types'][0].includes('A9') && !b1['types'][0].includes('A9')) {
				flip = true
			} else if ( ( b1['types'][0].includes('A2') || b1['types'][0].includes('A3') || b1['types'][0].includes('A4') ) &&
				( b2['types'][0].includes('A0') || b2['types'][0].includes('A0.') || b2['types'][0].includes('A0-') || b2['types'][0].includes('A0+') )  ) {
				flip = true
			} else {
				flip = false
			}
		}

		if (flip) {
			a['bit1'] = b2
			a['bit2'] = b1
		}
		else {
			a['bit1'] = b1
			a['bit2'] = b2
		}

		return a
	}
	
	DiscussionTracker.prototype.init_disc = function (discLetter) {
		this.disc = $discs[discLetter]
		this.dLet = discLetter
		let disc = this.disc

		this.conversion = []
		this.conversion.push(['[T]', 'Teacher'])
		this.conversion.push(['[K]', 'KaYeon'])
		this.conversion.push(['[H]', 'HwiYoung'])

		for (i in disc['randomRolls']) {

			if (disc['randomRolls'][i]['type'] === 'reader') {
				var rand = Math.floor(Math.random()*2)
				if (rand === 0) {
					this.conversion.push([disc['randomRolls'][i]['names'][0], 'HwiYoung'])
					this.conversion.push([disc['randomRolls'][i]['names'][1], 'KaYeon'])
				} else {
					this.conversion.push([disc['randomRolls'][i]['names'][0], 'KaYeon'])
					this.conversion.push([disc['randomRolls'][i]['names'][1], 'HwiYoung'])
				}
			} else if (disc['randomRolls'][i]['type'] === 'choose') {
				var x = 0
				var rolls = []
				while (x < disc['randomRolls'][i]['choose']) {
					x += 1
					var redundant = true
					var count = 0
					while (redundant && count < 50) {
						count += 1
						var rand = Math.floor(Math.random()*disc['randomRolls'][i]['of'])
						redundant = false
						for (k in rolls){
							if (rolls[k] === rand) {
								redundant = true
							}
						}
					}
					rolls.push(rand)
				}
				for (j in disc['randomRolls'][i]['names']){
					function fjs1 (a) { return a === disc['randomRolls'][i]['names'][j] }
					this.conversion.push([disc['randomRolls'][i]['names'][j], disc['randomRolls'][i]['bank'][rolls[disc['randomRolls'][i]['names'].findIndex(fjs1)]] ])
				}
			}
		}

		return
	}

	DiscussionTracker.prototype.discuss = function (text){
		var conv = this.conversion
		if (text === null || text === undefined){
			return
		}
		for (i in text) {
			if (text[i]['speaker'] === '[P]') {
				console.log(text[i]['line'])
				var x = 0
				while ( !(1 <= x && x <= text[i]['branch'].length) ) {
					x = readline.question("(enter code)");
				}
				x = x - 1
				__disc__(conv, text[i]['branch'][x])
			} else {
				var string = text[i]['line']
				for (j in conv) {
					if (string.includes(conv[j][0])) {
						string = string.replace(conv[j][0], conv[j][1])
					}
				}
				for (j in conv) {
					if (string.includes(conv[j][0])) {
						string = string.replace(conv[j][0], conv[j][1])
					}
				}
				this.queueLine(text[i]['speaker'], this.parse(string) )
			}
		}
		return
	}

	var alias_loadDB = DataManager.loadDatabase;
	DataManager.loadDatabase = function() {
		
		DataManager._databaseFiles.push({ name: '$p1b1', src: 'pluginData/part1beat1.json' });
		DataManager._databaseFiles.push({ name: '$p1b2', src: 'pluginData/part1beat2.json' });
		DataManager._databaseFiles.push({ name: '$p1b3', src: 'pluginData/part1beat3.json' });

		DataManager._databaseFiles.push({ name: '$p2b1', src: 'pluginData/part2beat1.json' });
		DataManager._databaseFiles.push({ name: '$p2b2', src: 'pluginData/part2beat2.json' });
		DataManager._databaseFiles.push({ name: '$p2b3', src: 'pluginData/part2beat3.json' });
		DataManager._databaseFiles.push({ name: '$p2b4', src: 'pluginData/part2beat4.json' });
		DataManager._databaseFiles.push({ name: '$p2b5', src: 'pluginData/part2beat5.json' });

		DataManager._databaseFiles.push({ name: '$p3b1', src: 'pluginData/part3beat1.json' });

		DataManager._databaseFiles.push({ name: '$p4b1', src: 'pluginData/part4beat1.json' });
		DataManager._databaseFiles.push({ name: '$p4b2', src: 'pluginData/part4beat2.json' });
		DataManager._databaseFiles.push({ name: '$p4b3', src: 'pluginData/part4beat3.json' });

		DataManager._databaseFiles.push({ name: '$ch', src: 'pluginData/characters.json' });
		DataManager._databaseFiles.push({ name: '$qu', src: 'pluginData/majorQ.json' });

		DataManager._databaseFiles.push({ name: '$discs', src: 'pluginData/discs.json' });

		alias_loadDB.call(this);
	};

	var tracker = new DiscussionTracker

	var alias_init = Game_System.prototype.initialize;
	Game_System.prototype.initialize = function() {
	    alias_init.call(this)

		tracker.init(null, $ch['characters'], $qu['majorQuestions'], $qu['transitionQuestions'])
		tracker.changePart(2)

	};

	ProcText.Plugin.askQuestion = function(args) {
		var partNum = Number(args[0])
		var beatNum = Number(args[1])
		var majMin = Number(args[2])
		var index = Number(args[3])
		tracker.changeBeat(beats[beatNum - 1]['beat'])

		if (majMin === 0) {
			tracker.askMajorQ()
		} else if (majMin === 1) {
			tracker.askProcGenQ(tracker.getMinorQ()[index - 1])
		}
	}

	ProcText.Plugin.inQueue = function() {
		return !!tracker.queue[0]
	}

	var alias_pluginCommand = Game_Interpreter.prototype.pluginCommand;
	Game_Interpreter.prototype.pluginCommand = function(command, args) {
		alias_pluginCommand.call(this, command, args);
		
		if (command.toLowerCase() === "ask") {
			$gameSwitches.setValue(17, true)
			ProcText.Plugin.askQuestion(args)
		} else if (command.toLowerCase() === "lines") {
			tracker.nextLine()
		} else if (command.toLowerCase() === "load_disc") {
			tracker.init_disc(args[0])
		} else if (command.toLowerCase() === "discuss") {
			tracker.discuss($discs[tracker.dLet]['randomText'][args[0] - 1])
		} else if (command.toLowerCase() === "load_part") {
			tracker.changePart(Number(args[0]))
		}
		return true;
	};

})();


Scene_Title.prototype.drawGameTitle = function() {
    var x = 20;
    var y = Graphics.height / 4;
    var maxWidth = Graphics.width - x * 2;
    var text = $dataSystem.gameTitle;
    this._gameTitleSprite.bitmap.outlineColor = 'black';
    this._gameTitleSprite.bitmap.outlineWidth = 8;
    this._gameTitleSprite.bitmap.fontSize = 72;
    this._gameTitleSprite.bitmap.drawText(text, x, y, maxWidth, 48, 'center');

    y += 72
    var text2 = "Acts I - II"
    this._gameTitleSprite.bitmap.fontSize = 44;
    this._gameTitleSprite.bitmap.drawText(text2, x, y, maxWidth, 48, 'center');
};

Scene_Title.prototype.createCommandWindow = function() {
    this._commandWindow = new Window_TitleCommand();
    this._commandWindow.setHandler('newGame',  this.commandNewGame.bind(this));
    this._commandWindow.setHandler('continue', this.commandContinue.bind(this));
    this._commandWindow.setHandler('credits',   this.commandCredits.bind(this));
    this._commandWindow.setHandler('options',  this.commandOptions.bind(this));
    this.addWindow(this._commandWindow);
};

Scene_Title.prototype.commandCredits = function() {
    SceneManager.push(Scene_Credits);
};

Window_TitleCommand.prototype.makeCommandList = function() {
    this.addCommand(TextManager.newGame,   'newGame');
    this.addCommand(TextManager.continue_, 'continue', this.isContinueEnabled());
    this.addCommand('Credits',			   'credits');
    this.addCommand(TextManager.options,   'options');
};

Window_Options.prototype.addGeneralOptions = function() {
    this.addCommand(TextManager.alwaysDash, 'alwaysDash');
    //this.addCommand(TextManager.commandRemember, 'commandRemember');
};

Window_Options.prototype.addVolumeOptions = function() {
    this.addCommand(TextManager.bgmVolume, 'bgmVolume');
    //this.addCommand(TextManager.bgsVolume, 'bgsVolume');
    //this.addCommand(TextManager.meVolume, 'meVolume');
    this.addCommand(TextManager.seVolume, 'seVolume');
};

Scene_Menu.prototype.create = function() {
    Scene_MenuBase.prototype.create.call(this);
    this.createCommandWindow();
    //this.createGoldWindow();
    this.createStatusWindow();
};

Window_MenuCommand.prototype.makeCommandList = function() {
    //this.addMainCommands();
    //this.addFormationCommand();
    this.addOptionsCommand();
    this.addSaveCommand();
    this.addOriginalCommands();
    this.addGameEndCommand();
};

Window_MenuCommand.prototype.addOriginalCommands = function() {
	//this.addCommand('Character Select', 'charsel');
	this.addCommand('Credits', 'credits');
};

Scene_Menu.prototype.createCommandWindow = function() {
    this._commandWindow = new Window_MenuCommand(0, 0);
    //this._commandWindow.setHandler('item',      this.commandItem.bind(this));
    //this._commandWindow.setHandler('skill',     this.commandPersonal.bind(this));
    //this._commandWindow.setHandler('equip',     this.commandPersonal.bind(this));
    //this._commandWindow.setHandler('status',    this.commandPersonal.bind(this));
    //this._commandWindow.setHandler('formation', this.commandFormation.bind(this));
    this._commandWindow.setHandler('options',   this.commandOptions.bind(this));
    //this._commandWindow.setHandler('charsel',   this.commandCharacterSelect.bind(this))
    this._commandWindow.setHandler('credits',   this.commandCredits.bind(this));
    this._commandWindow.setHandler('save',      this.commandSave.bind(this));
    this._commandWindow.setHandler('gameEnd',   this.commandGameEnd.bind(this));
    this._commandWindow.setHandler('cancel',    this.popScene.bind(this));
    this.addWindow(this._commandWindow);
};

Scene_Menu.prototype.commandCredits = function() {
    SceneManager.push(Scene_Credits);
};

Scene_Menu.prototype.commandCharacterSelect = function() {
    SceneManager.push(Scene_CharacterInput);
};

Window_Base.prototype.drawActorSimpleStatus = function(actor, x, y, width) {
    var lineHeight = this.lineHeight();
    var x2 = x + 180;
    //var width2 = Math.min(200, width - 180 - this.textPadding());
    this.drawActorName(actor, x, y);

    this.drawActorLevel(actor, x, y + lineHeight * 1);
    this.drawActorIcons(actor, x, y + lineHeight * 2);
    this.drawActorClass(actor, x2, y);
    //this.drawActorHp(actor, x2, y + lineHeight * 1, width2);
    //this.drawActorMp(actor, x2, y + lineHeight * 2, width2);
};

Game_Followers.prototype.initialize = function() {
    this._visible = $dataSystem.optFollowers;
    this._gathering = false;
    this._data = [];
    //for (var i = 1; i < $gameParty.maxBattleMembers(); i++) {
    //    this._data.push(new Game_Follower(i));
    //}
};

Window_Base.prototype.drawActorLevel = function(actor, x, y) {
    this.changeTextColor(this.systemColor());
    this.drawText(TextManager.levelA, x, y, 48);
    this.resetTextColor();
    //this.drawText(actor.level, x + 84, y, 36, 'right');
    this.drawText(actor.level, x + 104, y, 36, 'right');
};

// --------------------------------------------------------------------------
// Scene_Credits
//
// The scene class of the credits screen.

function Scene_Credits() {
    this.initialize.apply(this, arguments);
}

Scene_Credits.prototype = Object.create(Scene_MenuBase.prototype);
Scene_Credits.prototype.constructor = Scene_Credits;

Scene_Credits.prototype.create = function() {
	this.createWindowLayer()

	this._creditsWindow = new Window_Credits(30, 30)
	this._creditsBackWindow = new Window_CreditsBack(0, 1000)
	this._creditsBackWindow.setHandler('ok',     	this.popScene.bind(this))
    this._creditsBackWindow.setHandler('cancel',   	this.popScene.bind(this))
	this.addWindow(this._creditsWindow)
	this.addWindow(this._creditsBackWindow)
};

Scene_Credits.prototype.start = function() {
//    Scene_MenuBase.prototype.start.call(this);
//    this._statusWindow.refresh();
};

Scene_Credits.prototype.update = function() {
    Scene_Base.prototype.update.call(this);
};

//-----------------------------------------------------------------------------
// Window_Credits
//
// The window for displaying game credits.

function Window_Credits() {
    this.initialize.apply(this, arguments);
}

Window_Credits.prototype = Object.create(Window_Base.prototype);
Window_Credits.prototype.constructor = Window_Credits;

Window_Credits.prototype.initialize = function(x, y) {
    var width = 756
    var height = 564
    Window_Base.prototype.initialize.call(this, x, y, width, height);
    this.refresh();
};

Window_Credits.prototype.refresh = function() {
    var x = this.textPadding();
    var width = this.contents.width - this.textPadding() * 2;
    this.contents.clear();

    var size = 36
    this.contents.fontSize = size;
    var offsetY = 15;

    this.changeTextColor(this.systemColor());
    this.drawText('Design, Writing & Custom Code', 0, size*1 - offsetY, width, 'center');
    this.resetTextColor();
    this.contents.fontSize += 10
    this.drawText('Titus Thompson', 0, size*2, width, 'center');

    this.contents.fontSize = size;
    this.changeTextColor(this.systemColor());
    this.drawText('Plugins', 0, size*5 - offsetY, width, 'center');
    this.resetTextColor();
    this.drawText('HimeWorks           Yanfly  ', 0, size*6 - offsetY, width, 'center');
    this.drawText('  Galv           Yethwhinger', 0, size*7 - offsetY, width, 'center');
    this.drawText('taarna23                    ', 0, size*8 - offsetY, width, 'center');

    this.changeTextColor(this.systemColor());
    this.drawText('Title Graphic', 0, size*10 - offsetY, width, 'center');
    this.resetTextColor();
    this.drawText('きまぐれアフター', 0, size*11 - offsetY, width, 'center');

    this.changeTextColor(this.systemColor());
    this.drawText('Made with RPG Maker MV', 0, size*13 - offsetY, width, 'center');
};

Window_Credits.prototype.open = function() {
    this.refresh();
    Window_Base.prototype.open.call(this);
};

//-----------------------------------------------------------------------------
// Window_Credits
//
// The window for exiting game credits.

function Window_CreditsBack() {
    this.initialize.apply(this, arguments);
}

Window_CreditsBack.prototype = Object.create(Window_Command.prototype);
Window_CreditsBack.prototype.constructor = Window_Credits;

Window_CreditsBack.prototype.initialize = function(x, y) {
    var width = 100
    var height = 100
    Window_Command.prototype.initialize.call(this, x, y, width, height);
    this.addCommand('(back)', 'back');
    this.refresh();
};

Window_CreditsBack.prototype.refresh = function() {
    var x = this.textPadding();
    var width = this.contents.width - this.textPadding() * 2;
    this.contents.clear();
};

Window_CreditsBack.prototype.open = function() {
    this.refresh();
    Window_Base.prototype.open.call(this);
};


//-----------------------------------------------------------------------------
// Scene_CharacterInput
//
// The scene class of the character selection scene.

function Scene_CharacterInput() {
    this.initialize.apply(this, arguments);
}

Scene_CharacterInput.prototype = Object.create(Scene_MenuBase.prototype);
Scene_CharacterInput.prototype.constructor = Scene_CharacterInput;

Scene_CharacterInput.prototype.initialize = function() {
    Scene_MenuBase.prototype.initialize.call(this);
};

Scene_CharacterInput.prototype.prepare = function(actorId, maxLength) {
    //this._actorId = actorId;
    this._maxLength = maxLength;
};

Scene_CharacterInput.prototype.create = function() {
    Scene_MenuBase.prototype.create.call(this);
    //this._actor = $gameActors.actor(this._actorId);
    this.createInputWindow();
};

Scene_CharacterInput.prototype.start = function() {
    Scene_MenuBase.prototype.start.call(this);
    //this._editWindow.refresh();

    this._inputWindow.refresh();
};

Scene_CharacterInput.prototype.createInputWindow = function() {
    this._editWindow = new Window_CharacterEdit(this._actor, this._maxLength);
    this.addWindow(this._editWindow);

    this._inputWindow = new Window_CharacterInput(this._editWindow)
    this._inputWindow.setHandler('ok', this.onInputOk.bind(this));
	this.addWindow(this._inputWindow)
};

Scene_CharacterInput.prototype.onInputOk = function() {
	while ($gameParty.exists()) {
		$gameParty.removeActor($gameParty.leader().actorId())
	}
	var id = Window_CharacterInput.SENSEIS[this._inputWindow._index]
    $gameParty.addActor(id)
    if (id === 1) {
    	$gameVariables.setValue(10, id - 1)
    } else {
    	$gameVariables.setValue(10, id - 3)
    }
    $gameActors.actor(id).setName('')
    this.popScene();
};

Scene_Credits.prototype.update = function() {
    Scene_Base.prototype.update.call(this);
};


//-----------------------------------------------------------------------------
// Window_CharacterEdit
//
// The window for editing an actor's name on the name input screen.

function Window_CharacterEdit() {
    this.initialize.apply(this, arguments);
}

Window_CharacterEdit.prototype = Object.create(Window_Base.prototype);
Window_CharacterEdit.prototype.constructor = Window_CharacterEdit;

Window_CharacterEdit.prototype.initialize = function(actor, maxLength) {
    var width = this.windowWidth();
    var height = this.windowHeight();
    var x = (Graphics.boxWidth - width) / 2;
    var y = (Graphics.boxHeight - (height + this.fittingHeight(9) + 8)) / 2;
    Window_Base.prototype.initialize.call(this, x, y, width, height);
    this._actor = actor;
    this._name = '';
    this._index = this._name.length;
    this._maxLength = maxLength;
    this._defaultName = this._name;
    this.deactivate();
    this.refresh();
};

Window_CharacterEdit.prototype.windowWidth = function() {
    return 480;
};

Window_CharacterEdit.prototype.windowHeight = function() {
    return 42*3;
};

Window_CharacterEdit.prototype.name = function() {
    return this._name;
};

Window_CharacterEdit.prototype.restoreDefault = function() {
    this._name = this._defaultName;
    this._index = this._name.length;
    this.refresh();
    return this._name.length > 0;
};

Window_CharacterEdit.prototype.back = function() {
    if (this._index > 0) {
        this._index--;
        this._name = this._name.slice(0, this._index);
        this.refresh();
        return true;
    } else {
        return false;
    }
};

Window_CharacterEdit.prototype.refresh = function() {
    this.contents.clear();
    this.drawText('Select your character.  ', 0, this.contents.fontSize, this.windowWidth(), 'center');
};


//-----------------------------------------------------------------------------
// Window_CharacterInput
//
// The window for selecting text characters on the name input screen.

function Window_CharacterInput() {
    this.initialize.apply(this, arguments);
}

Window_CharacterInput.prototype = Object.create(Window_Selectable.prototype);
Window_CharacterInput.prototype.constructor = Window_CharacterInput;
Window_CharacterInput.SENSEIS =
        [ 1, 4, 5, 6,
          7, 8, 9, 10 ];

Window_CharacterInput.prototype.initialize = function(editWindow) {
    var x = (editWindow.x + editWindow.width / 2) - this.windowWidth() / 2;
    var y = editWindow.y + editWindow.height + 8;
    var width = this.windowWidth();
    var height = this.windowHeight();
    Window_Selectable.prototype.initialize.call(this, x, y, width, height);
    this._editWindow = editWindow;
    this._page = 0;
    this._index = 0;
    this.refresh();
    this.updateCursor();
    this.activate();
};

Window_CharacterInput.prototype.windowWidth = function() {
    return this.fittingHeight(16);
};

Window_CharacterInput.prototype.windowHeight = function() {
    return this.fittingHeight(8);
};

Window_CharacterInput.prototype.table = function() {
    return [Window_CharacterInput.SENSEIS]
};

Window_CharacterInput.prototype.maxCols = function() {
    return 4;
};

Window_CharacterInput.prototype.maxItems = function() {
    return 8;
};

Window_CharacterInput.prototype.isOk = function() {
    return true;
};

Window_CharacterInput.prototype.itemRect = function(index) {
    return {
        x: index % 4 * 144,
        y: Math.floor(index / 4) * 144,
        width: 144,
        height: 144
    };
};

Window_CharacterInput.prototype.refresh = function() {
    var table = this.table();
    this.contents.clear();
    this.resetTextColor();
    for (var i = 0; i < 8; i++) {
        var rect = this.itemRect(i);
        rect.x += 3;
        rect.width -= 6;
        this.drawActorFace($gameActors.actor(table[0][i]), rect.x, rect.y, 144, 144)
    }
};

Window_CharacterInput.prototype.updateCursor = function() {
    var rect = this.itemRect(this._index);
    this.setCursorRect(rect.x, rect.y, rect.width, rect.height);
};

Window_CharacterInput.prototype.isCursorMovable = function() {
    return this.active;
};

Window_CharacterInput.prototype.cursorDown = function(wrap) {
    if (this._index < 4 || wrap) {
        this._index = (this._index + 4) % 8;
    }
};

Window_CharacterInput.prototype.cursorUp = function(wrap) {
    if (this._index >= 4 || wrap) {
        this._index = (this._index - 4 + 8) % 8;
    }
};

Window_CharacterInput.prototype.cursorRight = function(wrap) {
    if (this._index % 4 < 3) {
        this._index++;
    } else if (wrap) {
        this._index -= 3;
    }
};

Window_CharacterInput.prototype.cursorLeft = function(wrap) {
    if (this._index % 4 > 0) {
        this._index--;
    } else if (wrap) {
        this._index += 3;
    }
};

Window_CharacterInput.prototype.processCursorMove = function() {
    var lastPage = this._page;
    Window_Selectable.prototype.processCursorMove.call(this);
    this.updateCursor();
    if (this._page !== lastPage) {
        SoundManager.playCursor();
    }
};

Window_CharacterInput.prototype.processHandling = function() {
    if (this.isOpen() && this.active) {
        if (Input.isRepeated('cancel')) {
            this.processBack();
        }
        if (Input.isRepeated('ok')) {
            this.processOk();
        }
    }
};

Window_CharacterInput.prototype.isCancelEnabled = function() {
    return true;
};

Window_CharacterInput.prototype.processCancel = function() {
    this.processBack();
};

Window_CharacterInput.prototype.processBack = function() {
    if (this._editWindow.back()) {
        SoundManager.playCancel();
    }
};

Window_CharacterInput.prototype.processOk = function() {
    if (this.isOk()) {
        this.onNameOk();
    }
};

Window_CharacterInput.prototype.onNameOk = function() {
    SoundManager.playOk();
    this.callOkHandler();
};
