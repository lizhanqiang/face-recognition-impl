import mysql.connector


class DatabaseHandler:
    def __init__(self):
        self.username = 'root'
        self.password = 'root'
        self.host = '192.168.56.101'
        self.port = '3306'
        self.database = 'facerecognition'
        self.charset = 'utf8'
        self.conn = None
        self.cursor = None

    def open_connection(self):
        self.conn = mysql.connector.connect(user=self.username,
                                            password=self.password,
                                            database=self.database,
                                            host=self.host,
                                            charset=self.charset)
        self.cursor = self.conn.cursor()

    def update(self, sql, param):
        if self.conn is None:
            self.open_connection()
        self.cursor.execute(sql, param)
        self.conn.commit()
        return self.cursor.rowcount

    def query(self, sql):
        if self.conn is None:
            self.open_connection()
        self.cursor.execute(sql)
        return self.cursor.fetchall()

    def close_connection(self):
        self.cursor.close()
        self.conn.close()


def add_face(id,name,feature,image):
    db_handler = DatabaseHandler()
    db_handler.update('insert into face_feature_repo(id,name,feature,image) values (%s,%s,%s,%s)',[id,name,feature,image])
    db_handler.close_connection()


def delete_face(face_id):
    db_handler = DatabaseHandler()
    db_handler.update('delete from face_feature_repo where id=%s',[face_id])
    db_handler.close_connection()


def query_face_list():
    db_handler = DatabaseHandler()
    face_list = db_handler.query('select id,name,feature from face_feature_repo')
    db_handler.close_connection()
    return face_list