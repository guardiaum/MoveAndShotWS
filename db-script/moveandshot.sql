--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.7
-- Dumped by pg_dump version 9.5.7

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: topology; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA topology;


ALTER SCHEMA topology OWNER TO postgres;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


--
-- Name: postgis; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS postgis WITH SCHEMA public;


--
-- Name: EXTENSION postgis; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION postgis IS 'PostGIS geometry, geography, and raster spatial types and functions';


--
-- Name: postgis_topology; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS postgis_topology WITH SCHEMA topology;


--
-- Name: EXTENSION postgis_topology; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION postgis_topology IS 'PostGIS topology spatial types and functions';


SET search_path = public, pg_catalog;

--
-- Name: poi_id_seq; Type: SEQUENCE; Schema: public; Owner: johny
--

CREATE SEQUENCE poi_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE poi_id_seq OWNER TO johny;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: poi; Type: TABLE; Schema: public; Owner: johny
--

CREATE TABLE poi (
    id integer DEFAULT nextval('poi_id_seq'::regclass) NOT NULL,
    poi_name character varying(100),
    poi_type character varying(100),
    latitude double precision,
    longitude double precision,
    poi_shot_area_polygon geometry,
    related_names character varying(300),
    image character varying(255)
);


ALTER TABLE poi OWNER TO johny;

--
-- Data for Name: poi; Type: TABLE DATA; Schema: public; Owner: johny
--

COPY poi (id, poi_name, poi_type, latitude, longitude, poi_shot_area_polygon, related_names, image) FROM stdin;
1	Home	Home	-8.04059003702074016	-34.9444761872291991	01030000000100000008000000F23451A3B61420C0000080EDDA7841C021599EA6C61420C000004028E07841C0029C29FAE11420C000000096DF7841C0AA1C14D0ED1420C0000040F6D57841C0DB04FEEDDC1420C0000040BBD47841C0EE985E63C41420C000004026D37841C08E7A38DBB71420C0000000BED57841C0F23451A3B61420C0000080EDDA7841C0	\N	null
\.


--
-- Name: poi_id_seq; Type: SEQUENCE SET; Schema: public; Owner: johny
--

SELECT pg_catalog.setval('poi_id_seq', 1, true);


--
-- Data for Name: spatial_ref_sys; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY spatial_ref_sys  FROM stdin;
\.


SET search_path = topology, pg_catalog;

--
-- Data for Name: topology; Type: TABLE DATA; Schema: topology; Owner: postgres
--

COPY topology  FROM stdin;
\.


--
-- Data for Name: layer; Type: TABLE DATA; Schema: topology; Owner: postgres
--

COPY layer  FROM stdin;
\.


SET search_path = public, pg_catalog;

--
-- Name: poi_pkey; Type: CONSTRAINT; Schema: public; Owner: johny
--

ALTER TABLE ONLY poi
    ADD CONSTRAINT poi_pkey PRIMARY KEY (id);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

