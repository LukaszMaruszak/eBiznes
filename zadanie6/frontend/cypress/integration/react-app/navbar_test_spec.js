describe('Navbar', function() {
    beforeEach(() => {
        cy.visit('http://localhost:3000')
    })

    it('Should be visible', function() {
        cy.get('.navbar').should('have.length', 1);
    });

    it('Should have appropriate title', function () {
        cy.get('.navbar__logo').should('have.text', ' The Gadget Zone ')
    })

    it('Should have shopping cart icon button', function () {
        cy.get('.shopping-cart').should('have.length', 1)
    })

    describe('Shopping cart icon button', function() {
        it('open new page after click', function () {
            cy.get('.shopping-cart').click();

            cy.url().should('include', '/shopping-cart')
        })
    })
})
