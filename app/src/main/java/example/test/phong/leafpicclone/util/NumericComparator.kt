package example.test.phong.leafpicclone.util

/**
 * Created by user on 2/14/2018.
 */
class NumericComparator {
    companion object {
        val TAG = "NumericComparator"
        /* The biggest Unicode character value we can have: http://unicode.org/faq/utf_bom.html#gen6 */
        private val UNICODE_MAX = 0x10FFFF

        /* Compare version strings:

  This function compares strings S1 and S2:
  1) By PREFIX in the same way as strcmp.
  2) Then by VERSION (most similarly to version compare of Debian's dpkg).
     Leading zeros in version numbers are ignored.
  3) If both (PREFIX and  VERSION) are equal, strcmp function is used for
     comparison. So this function can return 0 if (and only if) strings S1
     and S2 are identical.

  It returns number >0 for S1 > S2, 0 for S1 == S2 and number <0 for S1 < S2.

  This function compares strings, in a way that if VER1 and VER2 are version
  numbers and PREFIX and SUFFIX (SUFFIX defined as (\.[A-Za-z~][A-Za-z0-9~]*)*)
  are strings then VER1 < VER2 implies filevercmp (PREFIX VER1 SUFFIX,
  PREFIX VER2 SUFFIX) < 0.

  This function is intended to be a replacement for strverscmp. */

        fun filevercmp(s1: String, s2: String): Int {
            var s1 = s1
            var s2 = s2
            val s1_suffix: String
            val s2_suffix: String
            var s1_len: Int
            var s2_len: Int
            val result: Int

            /* easy comparison to see if strings are identical */
            val simple_cmp = strcmp(s1, s2)
            if (simple_cmp == 0)
                return 0

            /* special handle for "", "." and ".." */
            if (s1 == null || s1.length == 0)
                return -1
            if (s2 == null || s2.length == 0)
                return 1
            if (0 == strcmp(".", s1))
                return -1
            if (0 == strcmp(".", s2))
                return 1
            if (0 == strcmp("..", s1))
                return -1
            if (0 == strcmp("..", s2))
                return 1

            /* special handle for other hidden files */
            if (s1.codePointAt(0) == '.'.toInt() && s2.codePointAt(0) != '.'.toInt())
                return -1
            if (s1.codePointAt(0) != '.'.toInt() && s2.codePointAt(0) == '.'.toInt())
                return 1
            if (s1.codePointAt(0) == '.'.toInt() && s2.codePointAt(0) == '.'.toInt()) {
                s1 = s1.substring(1, s1.length)
                s2 = s2.substring(1, s2.length)
            }

            /* "cut" file suffixes */
            s1_suffix = match_suffix(s1)
            s2_suffix = match_suffix(s2)
            s1_len = s1.length - s1_suffix.length
            s2_len = s2.length - s2_suffix.length

            /* restore file suffixes if strings are identical after "cut" */
            if ((s1_suffix.length > 0 || s2_suffix.length > 0) && s1_len == s2_len && 0 == strncmp(s1, s2, s1_len)) {
                s1_len = s1.length
                s2_len = s2.length
            }

            result = verrevcmp(s1.substring(0, s1_len), s2.substring(0, s2_len))
            return if (result == 0) simple_cmp else result
        }

        /* The strncmp() function is similar to strcmp(), except it compares the only first (at most) n bytes of s1 and s2.
  */
        private fun strncmp(s1: String, s2: String, len: Int): Int {
            val len1 = Math.min(len, s1.length)
            val len2 = Math.min(len, s2.length)
            return s1.substring(0, len1).compareTo(s2.substring(0, len2))
        }

        /* slightly modified verrevcmp function from dpkg
  S1, S2 - compared string
  S1_LEN, S2_LEN - length of strings to be scanned

  This implements the algorithm for comparison of version strings
  specified by Debian and now widely adopted.  The detailed
  specification can be found in the Debian Policy Manual in the
  section on the 'Version' control field.  This version of the code
  implements that from s5.6.12 of Debian Policy v3.8.0.1
  http://www.debian.org/doc/debian-policy/ch-controlfields.html#s-f-Version */
        fun verrevcmp(s1: String, s2: String): Int {
            var s1_pos = 0
            var s2_pos = 0
            while (s1_pos < s1.length || s2_pos < s2.length) {
                var first_diff = 0
                while (s1_pos < s1.length && !c_isdigit(s1.codePointAt(s1_pos)) || s2_pos < s2.length && !c_isdigit(s2.codePointAt(s2_pos))) {
                    val s1_c = if (s1_pos >= s1.length) 0 else order(s1.codePointAt(s1_pos))
                    val s2_c = if (s2_pos >= s2.length) 0 else order(s2.codePointAt(s2_pos))
                    if (s1_c != s2_c)
                        return s1_c - s2_c
                    s1_pos++
                    s2_pos++
                }
                while (s1_pos < s1.length && s1.codePointAt(s1_pos) == '0'.toInt())
                    s1_pos++
                while (s2_pos < s2.length && s2.codePointAt(s2_pos) == '0'.toInt())
                    s2_pos++
                while (s1_pos < s1.length && s2_pos < s2.length
                        && c_isdigit(s1.codePointAt(s1_pos))
                        && c_isdigit(s2.codePointAt(s2_pos))) {
                    if (first_diff == 0)
                        first_diff = s1.codePointAt(s1_pos) - s2.codePointAt(s2_pos)
                    s1_pos++
                    s2_pos++
                }
                if (s1_pos < s1.length && c_isdigit(s1.codePointAt(s1_pos)))
                    return 1
                if (s2_pos < s2.length && c_isdigit(s2.codePointAt(s2_pos)))
                    return -1
                if (first_diff != 0)
                    return first_diff
            }
            return 0
        }

        /* verrevcmp helper function */
        private fun order(c: Int): Int {
            return if (c_isdigit(c))
                0
            else if (c_isalpha(c))
                c
            else if (c == '~'.toInt())
                -1
            else
                c + UNICODE_MAX + 1
        }


        /* Match a file suffix defined by this regular expression:
 /(\.[A-Za-z~][A-Za-z0-9~]*)*$/
 Returns empty string if not found. */
        private fun match_suffix(str: String): String {
            var str = str
            var match = ""
            var read_alpha = false
            while (str.length > 0) {
                if (read_alpha) {
                    read_alpha = false
                    if (!c_isalpha(str.codePointAt(0)) && '~'.toInt() != str.codePointAt(0))
                        match = ""
                } else if ('.'.toInt() == str.codePointAt(0)) {
                    read_alpha = true
                    if (match.length == 0)
                        match = str
                } else if (!c_isalnum(str.codePointAt(0)) && '~'.toInt() != str.codePointAt(0)) {
                    match = ""
                }
                str = str.substring(1, str.length)
            }
            return match
        }

        private fun c_isdigit(c: Int): Boolean {
            return Character.isDigit(c)
        }

        private fun c_isalpha(c: Int): Boolean {
            return Character.isLetter(c)
        }

        private fun c_isalnum(c: Int): Boolean {
            return Character.isLetterOrDigit(c)
        }

        /* The strcmp() function compares the two strings s1 and s2.
         It returns an integer less than, equal to, or greater than zero if s1 is found,
         respectively, to be less than, to match, or be greater than s2. */
        private fun strcmp(s1: String, s2: String): Int {
            return s1.compareTo(s2)
        }
    }
}