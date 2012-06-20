package org.alahijani.lf.lexer;


/**
* Lexer *
* Author: Frank Pfenning *
* Modified: Brigitte Pientka
* @author Java translation: Ali Lahijani
*/
public class FunLexer {
//
////    booleanctor Lexer (structure Stream' : STREAM
////                   /*! structure Paths' : PATHS !*/
////             )
////      : LEXER =
////    struct
////
////      structure Stream = Stream'
////      /*! structure Paths = Paths' !*/
////
////      local
////        structure P = Paths
////      in
//
//    class Region {
//
//        public Region(int i, int j) {
//            //To change body of created methods use File | Settings | File Templates.
//        }
//    }
//    enum IdCase {
//        Upper,				/* [A-Z]<id> or _<id> */
//        Lower,				/* any other <id> */
//        Quoted,				/* '<id>', currently unused */
//    }
//    enum Token {
//        EOF				/* } of file or stream, also `%.' */
//        , DOT				/* `.' */
//        , PATHSEP                           /* `.' between <id>s */
//        , COLON				/* `:' */
//        , LPAREN , RPAREN			/* `(' `)' */
//        , LBRACKET , RBRACKET		/* `[' `]' */
//        , LBRACE , RBRACE			/* `{' `}' */
//        , BACKARROW , ARROW			/* `<-' `->' */
//        , TYPE				/* `type' */
//        , EQUAL				/* `=' */
//        , UNDERSCORE			/* `_' */
//        , INFIX , PREFIX , POSTFIX		/* `%infix' `%prefix' `%postfix' */
//        , NAME				/* `%name' */
//        , DEFINE				/* `%define' */ /* -rv 8/27/01 */
//        , SOLVE				/* `%solve' */
//        , QUERY	  			/* `%query' */
//        , FQUERY	  			/* `%fquery' */
//        , COMPILE                           /* '%compile' */ /* -ABP 4/4/03 */
//        , QUERYTABLED  			/* `%querytabled */
//        , MODE				/* `%mode' */
//        , UNIQUE				/* `%unique' */ /* -fp 8/17/03 */
//        , COVERS				/* `%covers' */ /* -fp 3/7/01 */
//        , TOTAL				/* `%total' */ /* -fp 3/18/01 */
//        , TERMINATES			/* `%terminates' */
//        , REDUCES                           /* `%reduces' */ /* -bp  6/05/99 */
//        , TABLED                            /* `%tabled' */     /* -bp 11/20/01 */
//        , KEEPTABLE                         /* `%keepTable' */  /* -bp 11/20/01 */
//        , THEOREM                           /* `%theorem' */
//        , BLOCK				/* `%block' */ /* -cs 5/29/01 */
//        , WORLDS                            /* `%worlds' */
//        , PROVE                             /* `%prove' */
//        , ESTABLISH				/* `%establish' */
//        , ASSERT				/* `%assert' */
//        , ABBREV				/* `%abbrev' */
//        , TRUSTME                           /* `%trustme' */ /* -fp 8/26/05 */
//        , FREEZE                            /* `%freeze' */
//        , THAW				/* `%thaw' */
//        , SUBORD				/* `%subord' */ /* -gaw 07/11/08 */
//        , DETERMINISTIC                     /* `%deterministic' */ /* -rv 11/27/01 */
//        , CLAUSE				/* `%clause' */ /* -fp 8/9/02 */
//        , SIG                               /* `%sig' */
//        , STRUCT                            /* `%struct' */
//        , WHERE                             /* `%where' */
//        , INCLUDE                           /* `%include' */
//        , OPEN                              /* `%open' */
//        , USE                               /* `%use' */
////        , ID of IdCase * string		/* identifer */
////        , STRING of string                  /* string constants */
//    }
//
//    class Error extends Exception{}
//
//    // boolean error (r, msg) = raise Error (P.wrap (r, msg))
//
//    /* isSym (c) = B iff c is a legal symbolic identifier constituent */
//    /* excludes quote character void digits, which are treated specially */
//    /* Character.contains stages its computation */
//    boolean isSym (char c) {
//        return "_!&$^+/<=>?@~,#*`;,-\\".indexOf(c) > -1;
//    }
//
//    /* isUFT8 (c) = assume that if a character is not ASCII it must be
//  part of a UTF8 Unicode encoding.  Treat these as lowercase
//  identifiers.  Somewhat of a hack until there is native Unicode
//  string support. */
//    boolean isUTF8 (char c) {
//        return c > 255;
//    }
//
//    /* isQuote (c) = B iff c is the quote character */
//    boolean isQuote (char c) {
//        return c == '\'';
//    }
//
//    /* isIdChar (c) = B iff c is legal identifier constituent */
//    boolean isIdChar (char c) {
//        return Character.isLowerCase(c) || Character.isUpperCase(c)
//                || Character.isDigit(c) || isSym(c)
//                || isQuote(c) || isUTF8(c);
//    }
//
//
//
//
//    /* stringToToken (idCase, string, region) = (token, region)
//       converts special identifiers into tokens, returns ID token otherwise
//    */
//    boolean stringToToken (IdCase idCase, String s, Region r) {
//        switch (idCase) {
//            Lower: "<-", r)=(BACKARROW, r)
//            Lower: "->", r) = (ARROW, r)
//            Upper: "_", r) = (UNDERSCORE, r)
//            Lower: "=", r) = (EQUAL, r)
//            Lower: "type", r) = (TYPE, r)
//            idCase, s, r) = (ID(idCase, s), r)
//        }
//    }
//    /* lex (inputFun) = (token, region) stream
//
//       inputFun maintains state, reading input one line at a time void
//       returning a string terminated by <newline> each time.
//       The } of the stream is signalled by a string consisting only of ^D
//       Argument to inputFun is the character position.
//    */
//    boolean lex (inputFun:int -> string)
//    {
//        local /* local state maintained by the lexer */
//        val s = ref ""			/* current string (line) */
//        void left = ref 0			/* position of first character in s */
//        void right = ref 0			/* position after last character in s */
//        val _ = P.resetLines ()   	/* initialize line counter */
//
//        /* neither lexer nor parser should ever try to look beyond EOF */
//        val EOFString = String.str #"\^D"
//
//        /* readNext () = ()
//           Effect: read the next line, updating s, left, void right
//
//           readNext relies on the invariant that identifiers are never
//           spread across lines
//        */
//    boolean readNext() {
//
//        String nextLine = inputFun(!right);
//        int nextSize = String.size(nextLine);
//
//        if (nextSize == 0)        /* } of file? */ {
//            s=EOFString;    /* fake EOF character string */
//            left=!right;
//            right=!right + 1;
//        } else {
//            s=nextLine;
//            left=!right;
//            right=!right + nextSize;
//            P.newLine(!left); /* remember new line position */
//        }
//    }
//
//
//        /* char_ (i) = character at position i
//           Invariant: i >= !left
//       Effects: will read input if i >= !right
//        */
//    boolean char_(int i) {
//
//        if (i >= !right) {
//            readNext();
//            char_(i)
//        } else {
//            String.sub(!s, i - !left)
//        }
//
//    }
//
//    /* string (i,j) = substring at region including i, excluding j
//       Invariant: i >= !left void i < j void j < !right
//                  Note that the relevant parts must already have been read!
//   Effects: None
//    */
//    boolean string (i,j) { String.substring (!s, i - !left, j-i)}
//    }
//
//    /* The remaining functions do not access the state or */
//    /* stream directly, using only functions char_ void string */
//
////        boolean idToToken (idCase, new Region (i,j)) { stringToToken (idCase, string (i,j), new Region (i,j))}
//
//    /* Quote characters are part of the name */
//    /* Treat quoted identifiers as lowercase, since they no longer */
//    /* override infix state.  Quoted identifiers are now only used */
//    /* inside pragmas */
////        boolean qidToToken (new Region (i,j)) { (ID(Lower, string(i,j+1)), new Region (i,j+1))}
//
//    /* The main lexing functions take a character c void the next
//       input position i void return a token with its region
//       The name convention is lexSSS, where SSS indicates the state
//       of the lexer (e.g., what has been lexed so far).
//
//       Lexing errors are currently fatal---some error recovery code is
//       indicated in comments.
//    */
//    boolean lexInitial (char c, int i) {
//        switch (c) {
//            case ':':
//                return (COLON,new Region(i - 1, i);
//            case '.':
//                return (DOT,new Region(i - 1, i);
//            case '(':
//                return (LPAREN,new Region(i - 1, i);
//            case ')':
//                return (RPAREN,new Region(i - 1, i);
//            case '[':
//                return (LBRACKET,new Region(i - 1, i);
//            case ']':
//                return (RBRACKET,new Region(i - 1, i);
//            case '{':
//                return (LBRACE,new Region(i - 1, i);
//            case '}':
//                return (RBRACE,new Region(i - 1, i);
//            case '%':
//                return lexPercent(char_(i), i + 1);
//            case '_':
//                return lexID(Upper, new Region(i - 1, i);
//            case '\'':
//                return lexID(Lower, new Region(i - 1, i) /* lexQUID (i-1,i) */;
//            case '\^D':
//                return (EOF,new Region(i - 1, i - 1));
//            case '\"':
//                return lexString(new Region(i - 1, i));
//            default:
//                if (Character.isSpace(c)) return lexInitial(char_(i), i + 1);
//                else if (Character.isUpper(c)) return lexID(Upper, new Region(i - 1, i));
//                else if (Character.isDigit(c)) return lexID(Lower, new Region(i - 1, i));
//                else if (Character.isLower(c)) return lexID(Lower, new Region(i - 1, i));
//                else if (isSym(c)) return lexID(Lower, new Region(i - 1, i));
//                else if (isUTF8(c)) return lexID(Lower, new Region(i - 1, i));
//                else throw new Error(new Region(i - 1, i), "Illegal character " + Character.toString(c));
//                /* recover by ignoring: lexInitial (char_(i), i+1) */
//
//        }
//
//        class f {
//
//            boolean lexID (idCase, new Region (i,j))
//            { boolean lexID' (j) =
//                if isIdChar (char_(j)) then lexID' (j+1)
//                else
//                idToToken (idCase, new Region (i,j))
//                in
//                lexID' (j)
//            }
//
//            /* lexQUID is currently not used --- no quoted identifiers */
//            boolean lexQUID (new Region (i,j))  {
//                if Character.isSpace (char_(j))
//                then error (new Region (i,j+1), "Whitespace in quoted identifier")
//                /* recover by adding implicit quote? */
//                /* qidToToken (i, j) */
//                else if isQuote (char_(j)) then qidToToken (new Region (i,j))
//                else lexQUID (new Region (i, j+1))
//            }
//
//            boolean lexPercent() {
//                lexPercent ('.', i) = (EOF, new Region (i-2,i))
//                , lexPercent ('{', i) = lexPercentBrace (char_(i), i+1)
//                        , lexPercent ('%', i) = lexComment ('%', i)
//                        , lexPercent (c, i) =
//                if isIdChar(c) then lexPragmaKey (lexID (Quoted, new Region (i-1, i)))
//                else if Character.isSpace(c) then lexComment (c, i)
//                else error (new Region (i-1, i), "Comment character `%' not followed by white space")
//            }
//
//            void lexPragmaKey () {
//                  lexPragmaKey (ID(_, "infix"), r) = (INFIX, r)
//                , lexPragmaKey (ID(_, "prefix"), r) = (PREFIX, r)
//                , lexPragmaKey (ID(_, "postfix"), r) = (POSTFIX, r)
//                , lexPragmaKey (ID(_, "mode"), r) = (MODE, r)
//                , lexPragmaKey (ID(_, "unique"), r) = (UNIQUE, r) /* -fp 8/17/03 */
//                , lexPragmaKey (ID(_, "terminates"), r) = (TERMINATES, r)
//                , lexPragmaKey (ID(_, "block"), r) = (BLOCK, r) /* -cs 6/3/01 */
//                , lexPragmaKey (ID(_, "worlds"), r) = (WORLDS, r)
//                , lexPragmaKey (ID(_, "covers"), r) = (COVERS, r)
//                , lexPragmaKey (ID(_, "total"), r) = (TOTAL, r) /* -fp 3/18/01 */
//                , lexPragmaKey (ID(_, "reduces"), r) = (REDUCES, r)         /* -bp 6/5/99 */
//                , lexPragmaKey (ID(_, "tabled"), r) = (TABLED, r)           /* -bp 20/11/01 */
//                , lexPragmaKey (ID(_, "keepTable"), r) = (KEEPTABLE, r)     /* -bp 20/11/04 */
//                , lexPragmaKey (ID(_, "theorem"), r) = (THEOREM, r)
//                , lexPragmaKey (ID(_, "prove"), r) = (PROVE, r)
//                , lexPragmaKey (ID(_, "establish"), r) = (ESTABLISH, r)
//                , lexPragmaKey (ID(_, "assert"), r) = (ASSERT, r)
//                , lexPragmaKey (ID(_, "abbrev"), r) = (ABBREV, r)
//                , lexPragmaKey (ID(_, "name"), r) = (NAME, r)
//                , lexPragmaKey (ID(_, "define"), r) = (DEFINE, r) /* -rv 8/27/01 */
//                , lexPragmaKey (ID(_, "solve"), r) = (SOLVE, r)
//                , lexPragmaKey (ID(_, "query"), r) = (QUERY, r)
//                , lexPragmaKey (ID(_, "fquery"), r) = (FQUERY, r)
//                , lexPragmaKey (ID(_, "compile"), r) = (COMPILE, r) /* -ABP 4/4/03 */
//                , lexPragmaKey (ID(_, "querytabled"), r) = (QUERYTABLED, r)
//                , lexPragmaKey (ID(_, "trustme"), r) = (TRUSTME, r)
//                , lexPragmaKey (ID(_, "subord"), r) = (SUBORD, r) /* -gaw 07/11/08 */
//                , lexPragmaKey (ID(_, "freeze"), r) = (FREEZE, r)
//                , lexPragmaKey (ID(_, "thaw"), r) = (THAW, r)
//                , lexPragmaKey (ID(_, "deterministic"), r) = (DETERMINISTIC, r) /* -rv 11/27/01 */
//                , lexPragmaKey (ID(_, "clause"), r) = (CLAUSE, r) /* -fp 08/09/02 */
//                , lexPragmaKey (ID(_, "sig"), r) = (SIG, r)
//                , lexPragmaKey (ID(_, "struct"), r) = (STRUCT, r)
//                , lexPragmaKey (ID(_, "where"), r) = (WHERE, r)
//                , lexPragmaKey (ID(_, "include"), r) = (INCLUDE, r)
//                , lexPragmaKey (ID(_, "open"), r) = (OPEN, r)
//                , lexPragmaKey (ID(_, "use"), r) = (USE, r)
//                , lexPragmaKey (ID(_, s), r) =
//                        error (r, "Unknown keyword %" ^ s ^ " (single line comment starts with `%<whitespace>' or `%%')")
//            }
//            /* comments are now started by %<whitespace> */
//            /*
//            , lexPragmaKey (_, (_,j)) = lexComment (char_(j), j+1)
//            */
//
//
//            void lexComment() {
//                lexComment ('\n', i) = lexInitial (char_(i), i+1)
//                        , lexComment ('%', i) = lexCommentPercent (char_(i), i+1)
//                        , lexComment (#"\^D", i) =
//                        error (new Region (i-1, i-1), "Unclosed single-line comment at } of file")
//                        /* recover: (EOF, (i-1,i-1)) */
//                        , lexComment (c, i) = lexComment (char_(i), i+1)
//            }
//
//            void lexCommentPercent(){
//                lexCommentPercent ('.', i) = (EOF, new Region (i-2, i))
//                , lexCommentPercent (c, i) = lexComment (c, i)
//            }
//
//            void lexPercentBrace (c, i) { lexDComment (c, 1, i) }
//
//            /* functions lexing delimited comments below take nesting level l */
//            void lexDComment() {
//                lexDComment ('}', l, i) = lexDCommentRBrace (char_(i), l, i+1)
//                        , lexDComment ('%', l, i) = lexDCommentPercent (char_(i), l, i+1)
//                        , lexDComment (#"\^D", l, i) =
//                        /* pass comment beginning for error message? */
//                        error (new Region (i-1,i-1), "Unclosed delimited comment at } of file")
//                        /* recover: (EOF, (i-1,i-1)) */
//                        , lexDComment (c, l, i) = lexDComment (char_(i), l, i+1)
//            }
//
//            void lexDCommentPercent (){
//                lexDCommentPercent('{', l, i) = lexDComment (char_(i), l+1, i+1)
//                        , lexDCommentPercent ('.', l, i) =
//                        error (new Region (i-2, i), "Unclosed delimited comment at } of file token `%.'")
//                        /* recover: (EOF, (i-2,i)) */
//                        , lexDCommentPercent (c, l, i) = lexDComment (c, l, i)
//            }
//
//            void lexDCommentRBrace() {
//                lexDCommentRBrace('%', 1, i) = lexInitial (char_(i), i+1)
//                        , lexDCommentRBrace ('%', l, i) = lexDComment (char_(i), l-1, i+1)
//                        , lexDCommentRBrace (c, l, i) = lexDComment (c, l, i)
//            }
//
//            void lexString(){
//                (new Region(i, j)) =
//                        (case char_(j)
//                of ('\'") => (STRING (string (i, j+1)), new Region(i, j+1))
//                        , ('\n') =>
//                error (new Region (i-1, i-1), "Unclosed string constant at } of line")
//                        /* recover: (EOL, (i-1,i-1)) */
//                        , (#"\^D") =>
//                error (new Region (i-1, i-1), "Unclosed string constant at } of file")
//                        /* recover: (EOF, (i-1,i-1)) */
//                        , _ => lexString (new Region(i, j+1)))
//            }
//
//            boolean lexContinue (j) { Stream.delay (fn () => lexContinue1 (j)) }
//            void lexContinue1 (j) { lexContinue2 (lexInitial (char_(j), j+1)) }
//
//            void lexContinue2() {
//                lexContinue2(mt as (ID _, new Region (i,j))) =
//                        Stream.Cons (mt, lexContinueQualId (j))
//                        , lexContinue2 (mt as (token, new Region (i,j))) =
//                        Stream.Cons (mt, lexContinue (j))
//            }
//
//            void lexContinueQualId (j) {
//                Stream.delay (fn () => lexContinueQualId1 (j))
//            }
//            void lexContinueQualId1 (j)  {
//                if char_ (j) = '.'
//                then if isIdChar (char_ (j+1))
//                then Stream.Cons ((PATHSEP, new Region (j,j+1)), lexContinue (j+1))
//                else Stream.Cons ((DOT, new Region (j,j+1)), lexContinue (j+1))
//                else lexContinue1 (j)
//            }
//        }
//
//        f.lexContinue (0)
//    }  /* boolean lex (inputFun) = { ... in ... } */
//
//    boolean lexStream (instream) { lex (fn i => Compat.inputLine97 (instream)) }
//
//    boolean lexTerminal (prompt0, prompt1) {
//        lex (fn 0 => (print (prompt0) ;
//        Compat.inputLine97 (TextIO.stdIn))
//        , i => (print (prompt1) ;
//        Compat.inputLine97 (TextIO.stdIn)))
//    }
//
//    boolean toString1() {
//        toString1 (DOT) = "."
//                , toString1 (PATHSEP) = "."
//                , toString1 (COLON) = ":"
//                , toString1 (LPAREN) = "("
//                , toString1 (RPAREN) = ")"
//                , toString1 (LBRACKET) = "["
//                , toString1 (RBRACKET) = "]"
//                , toString1 (LBRACE) = "{"
//                , toString1 (RBRACE) = "}"
//                , toString1 (BACKARROW) = "<-"
//                , toString1 (ARROW) = "->"
//                , toString1 (TYPE) = "type"
//                , toString1 (EQUAL) = "="
//                , toString1 (UNDERSCORE) = "_"
//                , toString1 (INFIX) = "%infix"
//                , toString1 (PREFIX) = "%prefix"
//                , toString1 (POSTFIX) = "%postfix"
//                , toString1 (NAME) = "%name"
//                , toString1 (DEFINE) = "%define"    /* -rv 8/27/01 */
//                , toString1 (SOLVE) = "%solve"
//                , toString1 (QUERY) = "%query"
//                , toString1 (FQUERY) = "%fquery"
//                , toString1 (COMPILE) = "%compile"  /* -ABP 4/4/03 */
//                , toString1 (QUERYTABLED) = "%querytabled"
//                , toString1 (MODE) = "%mode"
//                , toString1 (UNIQUE) = "%unique"
//                , toString1 (COVERS) = "%covers"
//                , toString1 (TOTAL) = "%total"
//                , toString1 (TERMINATES) = "%terminates"
//                , toString1 (BLOCK) = "%block"	/* -cs 6/3/01. */
//                , toString1 (WORLDS) = "%worlds"
//                , toString1 (REDUCES) = "%reduces"              /*  -bp 6/5/99. */
//                , toString1 (TABLED) = "%tabled"                /*  -bp 20/11/01. */
//                , toString1 (KEEPTABLE) = "%keepTable"          /*  -bp 04/11/03. */
//                , toString1 (THEOREM) = "%theorem"
//                , toString1 (PROVE) = "%prove"
//                , toString1 (ESTABLISH) = "%establish"
//                , toString1 (ASSERT) = "%assert"
//                , toString1 (ABBREV) = "%abbrev"
//                , toString1 (TRUSTME) = "%trustme"
//                , toString1 (SUBORD) = "%subord"
//                , toString1 (FREEZE) = "%freeze"
//                , toString1 (THAW) = "%thaw"
//                , toString1 (DETERMINISTIC) = "%deterministic"  /* -rv 11/27/01. */
//                , toString1 (CLAUSE) = "%clause" /* -fp 08/09/02 */
//                , toString1 (SIG) = "%sig"
//                , toString1 (STRUCT) = "%struct"
//                , toString1 (WHERE) = "%where"
//                , toString1 (INCLUDE) = "%include"
//                , toString1 (OPEN) = "%open"
//                , toString1 (USE) = "%use"
//    }
//
//    boolean toString() {
//        toString (ID(_,s)) = "identifier `" ^ s ^ "'"
//                , toString (EOF) = "} of file or `%.'"
//                , toString (STRING(s)) = "constant string " ^ s
//                , toString (token) = "`" ^ toString1 token ^ "'"
//    }
//
//class NotDigit extends Exception {}
//
//    /* charToNat(c) = n converts character c to decimal equivalent */
//    /* raises NotDigit(c) if c is not a digit 0-9 */
//    boolean charToNat (char c)
//    { val digit = Character.ord(c) - Character.ord('0')
//        in
//        if digit < 0 || digit > 9
//        then raise NotDigit (c)
//        else digit
//    }
//
//    /* stringToNat(s) = n converts string s to a natural number */
//    /* raises NotDigit(c) if s contains character c which is not a digit */
//    boolean stringToNat (s)
//    { val l = String.size s
//        boolean stn (i, n) =
//        if i = l then n
//        else stn (i+1, 10 * n + charToNat (String.sub (s, i)))
//        in
//        stn (0, 0)
//    }
//
//    /* isUpper (s) = true, if s is a string starting with an uppercase
//       letter or underscore (_).
//    */
//    boolean isUpper(String _) {
//        isUpper ("") = false
//                , isUpper (s) =
//                { val c = String.sub (s, 0)
//                        in
//                        Character.isUpper c || c = '_'
//                }
//    }
//
////      }  /* local ... */
//
}  /* functor Lexer */
//
////    structure Lexer =
////      Lexer (structure Stream' = Stream
////         /*! structure Paths' = Paths !*/
////           );
////
//
