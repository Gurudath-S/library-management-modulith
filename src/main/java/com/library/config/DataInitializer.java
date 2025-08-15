package com.library.config;

import com.library.books.Book;
import com.library.books.BookService;
import com.library.transactions.Transaction;
import com.library.transactions.TransactionService;
import com.library.users.User;
import com.library.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userService.getAllUsers().isEmpty()) {
            initializeData();
        }
    }

    private void initializeData() {
        System.out.println("Initializing test data...");
        
        // Initialize users
        initializeUsers();
        
        // Initialize books
        initializeBooks();
        
        // Initialize transactions
        initializeTransactions();
        
        System.out.println("Test data initialization completed!");
        System.out.println("Users: " + userService.getAllUsers().size());
        System.out.println("Books: " + bookService.getAllBooks().size());
        System.out.println("Transactions: " + transactionService.getAllTransactions().size());
    }

    private void initializeUsers() {
        List<User> users = Arrays.asList(
            // Admin users
            createUser("admin","admin123", "admin@library.com", "Admin", "User", "ADMIN"),
            createUser("librarian", "librarian123", "librarian@library.com", "Librarian", "Manager", "LIBRARIAN"),

            // Regular users (46 more users to make 48 total)
            createUser("john_doe", "john.doe@email.com", "John", "Doe", "USER"),
            createUser("jane_smith", "jane.smith@email.com", "Jane", "Smith", "USER"),
            createUser("mike_johnson", "mike.johnson@email.com", "Mike", "Johnson", "USER"),
            createUser("sarah_wilson", "sarah.wilson@email.com", "Sarah", "Wilson", "USER"),
            createUser("david_brown", "david.brown@email.com", "David", "Brown", "USER"),
            createUser("emily_davis", "emily.davis@email.com", "Emily", "Davis", "USER"),
            createUser("chris_miller", "chris.miller@email.com", "Chris", "Miller", "USER"),
            createUser("lisa_taylor", "lisa.taylor@email.com", "Lisa", "Taylor", "USER"),
            createUser("robert_clark", "robert.clark@email.com", "Robert", "Clark", "USER"),
            createUser("amanda_white", "amanda.white@email.com", "Amanda", "White", "USER"),
            createUser("kevin_harris", "kevin.harris@email.com", "Kevin", "Harris", "USER"),
            createUser("jennifer_martin", "jennifer.martin@email.com", "Jennifer", "Martin", "USER"),
            createUser("matthew_garcia", "matthew.garcia@email.com", "Matthew", "Garcia", "USER"),
            createUser("michelle_rodriguez", "michelle.rodriguez@email.com", "Michelle", "Rodriguez", "USER"),
            createUser("andrew_lewis", "andrew.lewis@email.com", "Andrew", "Lewis", "USER"),
            createUser("stephanie_walker", "stephanie.walker@email.com", "Stephanie", "Walker", "USER"),
            createUser("joshua_hall", "joshua.hall@email.com", "Joshua", "Hall", "USER"),
            createUser("nicole_allen", "nicole.allen@email.com", "Nicole", "Allen", "USER"),
            createUser("daniel_young", "daniel.young@email.com", "Daniel", "Young", "USER"),
            createUser("jessica_king", "jessica.king@email.com", "Jessica", "King", "USER"),
            createUser("brandon_wright", "brandon.wright@email.com", "Brandon", "Wright", "USER"),
            createUser("ashley_lopez", "ashley.lopez@email.com", "Ashley", "Lopez", "USER"),
            createUser("ryan_green", "ryan.green@email.com", "Ryan", "Green", "USER"),
            createUser("megan_adams", "megan.adams@email.com", "Megan", "Adams", "USER"),
            createUser("tyler_baker", "tyler.baker@email.com", "Tyler", "Baker", "USER"),
            createUser("rachel_gonzalez", "rachel.gonzalez@email.com", "Rachel", "Gonzalez", "USER"),
            createUser("nathan_nelson", "nathan.nelson@email.com", "Nathan", "Nelson", "USER"),
            createUser("samantha_carter", "samantha.carter@email.com", "Samantha", "Carter", "USER"),
            createUser("jacob_mitchell", "jacob.mitchell@email.com", "Jacob", "Mitchell", "USER"),
            createUser("lauren_perez", "lauren.perez@email.com", "Lauren", "Perez", "USER"),
            createUser("ethan_roberts", "ethan.roberts@email.com", "Ethan", "Roberts", "USER"),
            createUser("kayla_turner", "kayla.turner@email.com", "Kayla", "Turner", "USER"),
            createUser("caleb_phillips", "caleb.phillips@email.com", "Caleb", "Phillips", "USER"),
            createUser("brittany_campbell", "brittany.campbell@email.com", "Brittany", "Campbell", "USER"),
            createUser("noah_parker", "noah.parker@email.com", "Noah", "Parker", "USER"),
            createUser("alexis_evans", "alexis.evans@email.com", "Alexis", "Evans", "USER"),
            createUser("lucas_edwards", "lucas.edwards@email.com", "Lucas", "Edwards", "USER"),
            createUser("courtney_collins", "courtney.collins@email.com", "Courtney", "Collins", "USER"),
            createUser("gabriel_stewart", "gabriel.stewart@email.com", "Gabriel", "Stewart", "USER"),
            createUser("brooke_sanchez", "brooke.sanchez@email.com", "Brooke", "Sanchez", "USER"),
            createUser("isaac_morris", "isaac.morris@email.com", "Isaac", "Morris", "USER"),
            createUser("sydney_rogers", "sydney.rogers@email.com", "Sydney", "Rogers", "USER"),
            createUser("mason_reed", "mason.reed@email.com", "Mason", "Reed", "USER"),
            createUser("jordan_cook", "jordan.cook@email.com", "Jordan", "Cook", "USER"),
            createUser("taylor_bailey", "taylor.bailey@email.com", "Taylor", "Bailey", "USER"),
            createUser("cameron_cooper", "cameron.cooper@email.com", "Cameron", "Cooper", "USER"),
            createUser("morgan_richardson", "morgan.richardson@email.com", "Morgan", "Richardson", "USER"),
            createUser("alex_cox", "alex.cox@email.com", "Alex", "Cox", "USER")
        );

        for (User user : users) {
            userService.createUser(user);
        }
    }

    private User createUser(String username, String email, String firstName, String lastName, String role) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode("password123"));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhoneNumber("555-0123");
        user.setAddress("123 Main St, City, State");
        user.setRole(User.Role.valueOf(role));
        user.setEnabled(true);
        user.setCreatedAt(LocalDateTime.now().minusDays(new Random().nextInt(365)));
        return user;
    }

    private User createUser(String username,String password ,String email, String firstName, String lastName, String role) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhoneNumber("555-0123");
        user.setAddress("123 Main St, City, State");
        user.setRole(User.Role.valueOf(role));
        user.setEnabled(true);
        user.setCreatedAt(LocalDateTime.now().minusDays(new Random().nextInt(365)));
        return user;
    }

    private void initializeBooks() {
        List<Book> books = Arrays.asList(
            // Programming & Technology
            createBook("978-0134685991", "Effective Java", "Joshua Bloch", "Programming", 3),
            createBook("978-0596009205", "Head First Design Patterns", "Eric Freeman", "Programming", 2),
            createBook("978-0132350884", "Clean Code", "Robert C. Martin", "Programming", 4),
            createBook("978-0201616224", "The Pragmatic Programmer", "David Thomas", "Programming", 2),
            createBook("978-0321356683", "Effective C++", "Scott Meyers", "Programming", 3),
            createBook("978-0596007126", "Beautiful Code", "Andy Oram", "Programming", 2),
            createBook("978-0596520687", "RESTful Web Services", "Leonard Richardson", "Programming", 2),
            createBook("978-1449355739", "MongoDB: The Definitive Guide", "Kristina Chodorow", "Programming", 2),
            createBook("978-0134757599", "Refactoring", "Martin Fowler", "Programming", 3),
            createBook("978-0321125215", "Domain-Driven Design", "Eric Evans", "Programming", 2),
            
            // Science & Mathematics
            createBook("978-0375727207", "A Brief History of Time", "Stephen Hawking", "Science", 4),
            createBook("978-0679732419", "The Elegant Universe", "Brian Greene", "Science", 3),
            createBook("978-0393317633", "Cosmos", "Carl Sagan", "Science", 5),
            createBook("978-0684801221", "The First Three Minutes", "Steven Weinberg", "Science", 2),
            createBook("978-0691125503", "Prime Numbers", "Richard Crandall", "Mathematics", 2),
            createBook("978-0486612720", "What Is Mathematics?", "Richard Courant", "Mathematics", 3),
            createBook("978-0062316097", "Sapiens", "Yuval Noah Harari", "Science", 6),
            createBook("978-0385537209", "The Signal and the Noise", "Nate Silver", "Mathematics", 2),
            createBook("978-0375758775", "The Code Book", "Simon Singh", "Mathematics", 3),
            createBook("978-0679745402", "Chaos", "James Gleick", "Science", 2),
            
            // Literature & Fiction
            createBook("978-0143127741", "To Kill a Mockingbird", "Harper Lee", "Fiction", 8),
            createBook("978-0547928227", "The Hobbit", "J.R.R. Tolkien", "Fiction", 5),
            createBook("978-0525478812", "The Great Gatsby", "F. Scott Fitzgerald", "Fiction", 6),
            createBook("978-0439064873", "Harry Potter and the Chamber of Secrets", "J.K. Rowling", "Fiction", 4),
            createBook("978-0060850524", "Brave New World", "Aldous Huxley", "Fiction", 3),
            createBook("978-0451524935", "1984", "George Orwell", "Fiction", 7),
            createBook("978-0142437179", "Of Mice and Men", "John Steinbeck", "Fiction", 4),
            createBook("978-0544003415", "The Lord of the Rings", "J.R.R. Tolkien", "Fiction", 3),
            createBook("978-0316769174", "The Catcher in the Rye", "J.D. Salinger", "Fiction", 5),
            createBook("978-0486280615", "Pride and Prejudice", "Jane Austen", "Fiction", 4),
            
            // History & Biography
            createBook("978-0684824901", "John Adams", "David McCullough", "Biography", 3),
            createBook("978-0375415036", "The Guns of August", "Barbara Tuchman", "History", 2),
            createBook("978-0679783121", "Team of Rivals", "Doris Kearns Goodwin", "Biography", 2),
            createBook("978-0674035805", "The Better Angels of Our Nature", "Steven Pinker", "History", 2),
            createBook("978-0679764021", "Guns, Germs, and Steel", "Jared Diamond", "History", 3),
            createBook("978-0743223133", "Einstein: His Life and Universe", "Walter Isaacson", "Biography", 3),
            createBook("978-0767905923", "The Immortal Life of Henrietta Lacks", "Rebecca Skloot", "Biography", 4),
            createBook("978-0679744689", "The Diary of a Young Girl", "Anne Frank", "Biography", 5),
            createBook("978-0684830421", "Undaunted Courage", "Stephen Ambrose", "History", 2),
            createBook("978-0385495226", "The Wright Brothers", "David McCullough", "Biography", 3),
            
            // Business & Economics
            createBook("978-0066620992", "Good to Great", "Jim Collins", "Business", 4),
            createBook("978-1259584015", "The Lean Startup", "Eric Ries", "Business", 3),
            createBook("978-0307463746", "Thinking, Fast and Slow", "Daniel Kahneman", "Business", 3),
            createBook("978-0062301680", "Zero to One", "Peter Thiel", "Business", 2),
            createBook("978-0316346627", "Outliers", "Malcolm Gladwell", "Business", 4),
            createBook("978-0470432907", "The Black Swan", "Nassim Nicholas Taleb", "Business", 2),
            createBook("978-0385348119", "Freakonomics", "Steven Levitt", "Economics", 3),
            createBook("978-0062206084", "The Everything Store", "Brad Stone", "Business", 2),
            createBook("978-0446576222", "Rich Dad Poor Dad", "Robert Kiyosaki", "Business", 5),
            createBook("978-0307887894", "The 4-Hour Workweek", "Timothy Ferriss", "Business", 3),
            
            // Art & Culture
            createBook("978-0500203063", "The Story of Art", "Ernst Gombrich", "Art", 2),
            createBook("978-0140447163", "Ways of Seeing", "John Berger", "Art", 3)
        );

        for (Book book : books) {
            bookService.createBook(book);
        }
    }

    private Book createBook(String isbn, String title, String author, String category, int totalCopies) {
        Book book = new Book();
        book.setIsbn(isbn);
        book.setTitle(title);
        book.setAuthor(author);
        book.setCategory(category);
        book.setTotalCopies(totalCopies);
        book.setAvailableCopies(totalCopies);
        book.setStatus(Book.BookStatus.AVAILABLE);
        book.setCreatedAt(LocalDateTime.now().minusDays(new Random().nextInt(90)));
        return book;
    }

    private void initializeTransactions() {
        List<User> users = userService.getAllUsers();
        List<Book> books = bookService.getAllBooks();
        Random random = new Random();

        // Create about 138 transactions (mix of completed and active)
        for (int i = 0; i < 138; i++) {
            try {
                User randomUser = users.get(random.nextInt(users.size()));
                Book randomBook = books.get(random.nextInt(books.size()));

                // Skip if this user already has this book borrowed
                if (transactionService.hasActiveTransaction(randomUser.getId(), randomBook.getId())) {
                    continue;
                }

                // Skip if book has no available copies
                if (randomBook.getAvailableCopies() <= 0) {
                    continue;
                }

                // 70% chance to create a completed transaction, 30% active
                if (random.nextDouble() < 0.7) {
                    // Create a completed transaction (borrow and return)
                    transactionService.borrowBookEntity(randomUser.getId(), randomBook.getId());
                    transactionService.returnBookEntity(randomUser.getId(), randomBook.getId());
                } else {
                    // Create an active transaction (just borrow)
                    transactionService.borrowBookEntity(randomUser.getId(), randomBook.getId());
                }

            } catch (Exception e) {
                // Skip this transaction and continue
                System.out.println("Skipping transaction due to error: " + e.getMessage());
            }
        }
    }
}
