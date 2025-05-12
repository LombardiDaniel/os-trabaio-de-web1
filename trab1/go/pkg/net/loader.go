package net

import (
	"bytes"
	"log/slog"
	"net/http"
	"net/textproto"
	"strconv"
	"strings"
)

const (
	crlf       string = "\r\n"
	doubleCrlf string = crlf + crlf
)

func Parse(b []byte) (http.Request, error) {

	var req http.Request

	// seperate in reqHeader and reqBody
	blocks := bytes.SplitN(b, []byte(doubleCrlf), 2)

	if len(blocks) >= 1 {
		linesStr := string(blocks[0])
		lines := strings.Split(linesStr, crlf)

		if len(lines) == 0 {
			return req, nil
		}

		reqLine := lines[0]
		words := strings.Split(reqLine, " ")
		switch len(words) {
		case 1:
			req.Method = words[0]
		case 2:
			req.Method = words[0]
			req.RequestURI = words[1]
		}

		if len(lines) > 1 { // header is more than just request line
			for _, headerLine := range lines[1:] {
				words := strings.SplitN(headerLine, ": ", 2)
				if len(words) <= 1 {
					continue
				}
				k := textproto.CanonicalMIMEHeaderKey(words[0])
				req.Header.Add(k, words[1])
			}
		}
	}

	if len(blocks) >= 2 {
		req.Body = bytes.Join(blocks[1:], []byte(""))
	}

	contentLenHs, ok := req.Header["Content-Length"]
	if ok && len(contentLenHs) > 0 {
		contentLenInt, err := strconv.Atoi(contentLenHs[0])
		if err != nil {
			slog.Warn("invalid Content-Length header")
		} else {
			req.Body = req.Body[:contentLenInt]
		}
	}

	return req
	panic("not impl")
}
