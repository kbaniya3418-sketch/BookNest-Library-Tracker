// Run in mongosh:  load("sample-data.js")
const dbRef = db.getSiblingDB("booknestdb");

dbRef.students.deleteMany({});
dbRef.borrowings.deleteMany({});

const s1 = ObjectId(), s2 = ObjectId(), s3 = ObjectId();

dbRef.students.insertMany([
  { _id: s1, name: "Anita Sharma",  email: "anita@example.com",  age: 20, department: "SCIENCE",     joinDate: "2026-01-10", active: true },
  { _id: s2, name: "Bikash Thapa",  email: "bikash@example.com", age: 22, department: "ENGINEERING", joinDate: "2026-02-05", active: true },
  { _id: s3, name: "Sita Gurung",   email: "sita@example.com",   age: 19, department: "ARTS",        joinDate: "2026-03-15", active: true }
]);

dbRef.borrowings.insertMany([
  { studentId: s1.toString(), bookTitle: "A Brief History of Time", author: "Stephen Hawking", genre: "Science",
    borrowDate: "2026-09-01", dueDate: "2026-09-15", returned: true,  returnDate: "2026-09-14", fine: 0 },
  { studentId: s1.toString(), bookTitle: "Cosmos", author: "Carl Sagan", genre: "Science",
    borrowDate: "2026-09-20", dueDate: "2026-10-04", returned: false, returnDate: null, fine: 0 },
  { studentId: s2.toString(), bookTitle: "Clean Code", author: "Robert C. Martin", genre: "Technology",
    borrowDate: "2026-10-01", dueDate: "2026-10-15", returned: false, returnDate: null, fine: 0 },
  { studentId: s3.toString(), bookTitle: "Muna Madan", author: "Laxmi Prasad Devkota", genre: "Poetry",
    borrowDate: "2026-10-10", dueDate: "2026-10-24", returned: false, returnDate: null, fine: 0 }
]);

print("Students: " + dbRef.students.countDocuments() + ", Borrowings: " + dbRef.borrowings.countDocuments());
